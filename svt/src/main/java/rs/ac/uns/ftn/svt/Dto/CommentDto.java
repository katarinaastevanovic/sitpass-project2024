package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.Comment;
import rs.ac.uns.ftn.svt.Model.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentDto {

    private long id;
    private String text;
    private LocalDateTime createdAt;
    private Long parentId; // Optional field for replies
    private Long reviewId; // ID of the associated review
    private String userEmail;
    private List<CommentDto> replies;


    public Comment convertToModel(Review review, Comment parent) {
        return Comment.builder()
                .id(getId())
                .text(getText())
                .createdAt(getCreatedAt() != null ? getCreatedAt() : LocalDateTime.now()) // Default to current time
                .parent(parent) // Parent can be null if it's a top-level comment
                .review(review)
                .build();
    }

    public static CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .reviewId(comment.getReview().getId())
                .userEmail(comment.getUser().getEmail())  // Prikaz korisničkog email-a
                .replies(comment.getReplies() != null ? comment.getReplies().stream().map(CommentDto::convertToDto).collect(Collectors.toList()) : null)
                .build();
    }

}
