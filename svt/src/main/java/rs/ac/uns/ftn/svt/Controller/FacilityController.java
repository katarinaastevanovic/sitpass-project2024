package rs.ac.uns.ftn.svt.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.Dto.ExcerciseDto;
import rs.ac.uns.ftn.svt.Dto.FacilityDto;
import rs.ac.uns.ftn.svt.Dto.ReviewDto;
import rs.ac.uns.ftn.svt.Dto.UserDto;
import rs.ac.uns.ftn.svt.Exception.UnauthorizedException;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Repository.FacilityRepository;
import rs.ac.uns.ftn.svt.Repository.ManagesRepository;
import rs.ac.uns.ftn.svt.Repository.UserRepository;
import rs.ac.uns.ftn.svt.Service.FacilityService;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.uns.ftn.svt.Model.User;
import rs.ac.uns.ftn.svt.Model.Administrator;
import rs.ac.uns.ftn.svt.Exception.NotFoundException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final UserRepository userRepository;
    private final  FacilityRepository facilityRepository;
    private final ManagesRepository managesRepository;



    @GetMapping
    public ResponseEntity<List<FacilityDto>> getFacilities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Provera da li je korisnik administrator
        if (currentUser instanceof Administrator) {
            // Ako je administrator, vraćamo sve objekte
            return ResponseEntity.ok(facilityService.getAll());
        } else {
            // Ako je običan korisnik, vraćamo samo aktivne objekte
            return ResponseEntity.ok(facilityService.getAllActive());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity <FacilityDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(facilityService.getOneById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<FacilityDto> createFacility(@RequestBody FacilityDto facilityDto) {
         return ResponseEntity.status(CREATED).body(facilityService.create(facilityDto));
    }

    @GetMapping("/search/byCity")
    public ResponseEntity<List<FacilityDto>> searchByCity(@RequestParam String city) {
        return ResponseEntity.ok(facilityService.searchByCity(city));
    }


    @GetMapping("/search/byDiscipline")
    public ResponseEntity<List<FacilityDto>> searchByDiscipline(@RequestParam String discipline) {
        List<FacilityDto> facilities = facilityService.searchByDiscipline(discipline);
        return ResponseEntity.ok(facilities);
    }

    @GetMapping("/search/byRatingRange")
    public ResponseEntity<List<FacilityDto>> searchByRatingRange(@RequestParam Double minRating, @RequestParam Double maxRating) {
        return ResponseEntity.ok(facilityService.searchByRatingRange(minRating, maxRating));
    }


    @GetMapping("/search/byWorkDay")
    public ResponseEntity<List<FacilityDto>> searchByWorkDay(@RequestParam String workDay) {
        List<FacilityDto> facilities = facilityService.searchByWorkDay(workDay);
        return ResponseEntity.ok(facilities);
    }
    @GetMapping("/search/byWorkTime")
    public ResponseEntity<List<FacilityDto>> searchByWorkTime(@RequestParam String day, @RequestParam String fromTime, @RequestParam String untilTime) {
        return ResponseEntity.ok(facilityService.searchByWorkTime(day, fromTime, untilTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityDto> updateFacility(@PathVariable Long id, @RequestBody FacilityDto facilityDto, Principal principal) {
        // Get the logged-in user by email (from the principal)
        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Check if the user is an admin using Spring Security authorities
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // Check if the user is a manager of the facility
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facility not found"));

        boolean isManager = managesRepository.existsByUserAndFacility(user, facility);

        // Allow if the user is either an admin or a manager of the facility
        if (isAdmin || isManager) {
            FacilityDto updatedFacility = facilityService.update(id, facilityDto);
            return ResponseEntity.ok(updatedFacility);
        } else {
            throw new UnauthorizedException("You don't have permission to edit this facility");
        }
    }
    @GetMapping("/{userId}/is-manager")
    public ResponseEntity<Boolean> checkIfManager(@PathVariable Long userId) {
        boolean isManager = managesRepository.existsByUserId(userId);
        return ResponseEntity.ok(isManager);
    }

    @GetMapping("/{facilityId}/managers")
    public List<UserDto> getFacilityManagers(@PathVariable Long facilityId) {
        return facilityService.getManagersByFacilityId(facilityId);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ExcerciseDto> createExcercise(@RequestBody ExcerciseDto excerciseDto, Principal principal) {
        ExcerciseDto createdExcercise = facilityService.createExcercise(excerciseDto, principal);
        return ResponseEntity.status(CREATED).body(createdExcercise);
    }


    @PostMapping("/rate")
    public ResponseEntity<Map<String, Object>> rateFacility(@RequestBody ReviewDto reviewDto, Principal principal) {
        ReviewDto createdReview = facilityService.createReview(reviewDto, principal);

        // Kreiraj mapu koja sadrži reviewId i ostale podatke o oceni
        Map<String, Object> response = new HashMap<>();
        response.put("reviewId", createdReview.getId()); // Pretpostavljamo da `ReviewDto` ima `getId()`
        response.put("review", createdReview);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }




    @PostMapping("/{facilityId}/add-manager")
    public ResponseEntity<Void> assignManager(@PathVariable Long facilityId, @RequestParam Long userId) {
        facilityService.assignManager(facilityId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{facilityId}/remove-manager/{userId}")
    public ResponseEntity<Void> removeManager(@PathVariable Long facilityId, @PathVariable Long userId) {
        facilityService.removeManager(facilityId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ExcerciseDto>> getExcerciseHistoryByUserId(@PathVariable Long userId) {
        List<ExcerciseDto> excerciseHistory = facilityService.getExcerciseHistoryByUserId(userId);
        return ResponseEntity.ok(excerciseHistory);
    }

    @GetMapping("/manager/{userId}/facility/{facilityId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsForManagerFacility(@PathVariable Long userId, @PathVariable Long facilityId) {
        List<ReviewDto> reviews = facilityService.getAllReviewsForManagerFacility(userId, facilityId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        facilityService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }


}
