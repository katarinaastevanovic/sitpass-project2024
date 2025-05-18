package rs.ac.uns.ftn.svt.Service;


import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Dto.ExcerciseDto;
import rs.ac.uns.ftn.svt.Dto.FacilityDto;
import rs.ac.uns.ftn.svt.Dto.ReviewDto;
import rs.ac.uns.ftn.svt.Dto.UserDto;
import rs.ac.uns.ftn.svt.Model.Facility;

import java.security.Principal;
import java.util.List;

public interface FacilityService {
    List<FacilityDto> getAll();

    List<FacilityDto>getAllActive();

    FacilityDto getOneById(Long id);

    void delete(Long id);

    FacilityDto create(FacilityDto facilityDto);

    List<FacilityDto> searchByCity(String city);

    List<FacilityDto> searchByDiscipline(String discipline);

    List<FacilityDto> searchByRatingRange(Double minRating, Double maxRating);

    List<FacilityDto> searchByWorkDay(String workDay);

    List<FacilityDto> searchByWorkTime(String day, String fromTime, String untilTime);

    FacilityDto update(Long id, FacilityDto facilityDto);



    @Transactional
    ExcerciseDto createExcercise(ExcerciseDto excerciseDto, Principal principal);

    @Transactional
    ReviewDto createReview(ReviewDto reviewDto, Principal principal);


    @Transactional
    void assignManager(Long facilityId, Long userId);

    List<UserDto> getManagersByFacilityId(Long facilityId);

    void removeManager(Long facilityId, Long userId);

    List<ExcerciseDto> getExcerciseHistoryByUserId(Long userId);


    @Transactional
    List<ReviewDto> getAllReviewsForManagerFacility(Long userId, Long facilityId);

    @Transactional
    void deleteReview(Long reviewId);
}
