package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.Review;
import rs.ac.uns.ftn.svt.Model.User;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDto {

    private Long id;
    private RateDto rate;
    private Long userId;
    private Long facilityId;
    private LocalDateTime createdAt;
    private Integer excerciseCount;
    private Boolean hidden;


    public Review convertToModel() {
        return Review.builder()
                .id(getId())
                .rate(getRate() != null ? getRate().convertToModel() : null)
                .user(User.builder().id(getUserId()).build())  // Pretpostavka da je korisnik kreiran sa samo ID-jem
                .facility(Facility.builder().id(getFacilityId()).build())  // Pretpostavka da je objekat kreiran sa samo ID-jem
                .createdAt(getCreatedAt())
                .excerciseCount(getExcerciseCount())
                .hidden(getHidden())
                .build();
    }



    public static ReviewDto convertToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rate(RateDto.convertToDto(review.getRate()))
                .userId(review.getUser().getId())
                .facilityId(review.getFacility().getId())
                .createdAt(review.getCreatedAt())
                .excerciseCount(review.getExcerciseCount())
                .hidden(review.getHidden())
                .build();
    }
}
