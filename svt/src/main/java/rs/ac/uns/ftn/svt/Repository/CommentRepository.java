package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReviewId(Long reviewId);
    void deleteByReviewId(Long reviewId);
}
