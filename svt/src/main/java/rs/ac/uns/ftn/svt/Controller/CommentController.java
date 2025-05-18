package rs.ac.uns.ftn.svt.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.Dto.CommentDto;
import rs.ac.uns.ftn.svt.Service.CommentService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // Endpoint za kreiranje komentara
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, Principal principal) {
        if (commentDto.getReviewId() == null) {
            return ResponseEntity.badRequest().body(null); // Provera za validnost `reviewId`
        }

        // Prosleđivanje `principal` objekta da bi se koristio u metodi createComment
        CommentDto createdComment = commentService.createComment(commentDto, principal);
        return ResponseEntity.ok(createdComment);
    }

    // Endpoint za dobavljanje svih komentara za određeni facility ID
    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<CommentDto>> getCommentsByFacilityId(@PathVariable Long facilityId) {
        List<CommentDto> comments = commentService.getCommentsByFacilityId(facilityId);
        return ResponseEntity.ok(comments);
    }



    // Endpoint za dobavljanje svih komentara za određenu recenziju
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentDto>> getCommentsByReviewId(@PathVariable Long reviewId) {
        List<CommentDto> comments = commentService.getCommentsByReviewId(reviewId);
        return ResponseEntity.ok(comments);
    }
}
