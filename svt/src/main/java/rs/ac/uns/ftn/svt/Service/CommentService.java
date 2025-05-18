package rs.ac.uns.ftn.svt.Service;

import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Dto.CommentDto;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    @Transactional
    CommentDto createComment(CommentDto commentDto, Principal principal);

    List<CommentDto> getCommentsByReviewId(Long reviewId);

    @Transactional(readOnly = true)
    List<CommentDto> getCommentsByFacilityId(Long facilityId);
}
