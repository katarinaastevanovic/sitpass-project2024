package rs.ac.uns.ftn.svt.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Dto.CommentDto;
import rs.ac.uns.ftn.svt.Exception.NotFoundException;
import rs.ac.uns.ftn.svt.Model.Comment;
import rs.ac.uns.ftn.svt.Model.Review;
import rs.ac.uns.ftn.svt.Model.User;
import rs.ac.uns.ftn.svt.Repository.CommentRepository;
import rs.ac.uns.ftn.svt.Repository.ReviewRepository;
import rs.ac.uns.ftn.svt.Repository.UserRepository;
import rs.ac.uns.ftn.svt.Service.CommentService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    public final UserRepository userRepository;

    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto, Principal principal) {
        // Pronađi trenutno ulogovanog korisnika
        String email = principal.getName();  // Pretpostavljamo da JWT token sadrži email korisnika

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        // Nađi recenziju za koju se dodaje komentar
        Review review = reviewRepository.findById(commentDto.getReviewId())
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + commentDto.getReviewId()));

        // Nađi parent komentar ako postoji
        Comment parent = null;
        if (commentDto.getParentId() != null) {
            parent = commentRepository.findById(commentDto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent comment not found with id: " + commentDto.getParentId()));
        }

        // Kreiraj komentar i poveži ga sa korisnikom
        Comment comment = commentDto.convertToModel(review, parent);
        comment.setUser(user);  // Poveži komentar sa korisnikom koji ga je kreirao
        comment = commentRepository.save(comment);

        return CommentDto.convertToDto(comment);  // Vrati DTO sa korisničkim emailom
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByReviewId(Long reviewId) {
        return commentRepository.findByReviewId(reviewId)
                .stream()
                .map(CommentDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getCommentsByFacilityId(Long facilityId) {
        // Prvo nađi sve recenzije za dati facilityId
        List<Review> reviews = reviewRepository.findByFacilityId(facilityId);

        if (reviews.isEmpty()) {
            throw new NotFoundException("No reviews found for facility with id: " + facilityId);
        }

        // Prikupljanje svih komentara za svaku recenziju
        return reviews.stream()
                .flatMap(review -> commentRepository.findByReviewId(review.getId()).stream())
                .map(CommentDto::convertToDto)
                .collect(Collectors.toList());
    }

}
