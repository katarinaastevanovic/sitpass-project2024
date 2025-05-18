package rs.ac.uns.ftn.svt.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Dto.*;
import rs.ac.uns.ftn.svt.Exception.BadRequestException;
import rs.ac.uns.ftn.svt.Exception.NotFoundException;
import rs.ac.uns.ftn.svt.Exception.UnauthorizedException;
import rs.ac.uns.ftn.svt.Model.*;
import rs.ac.uns.ftn.svt.Repository.*;
import rs.ac.uns.ftn.svt.Service.DisciplineService;
import rs.ac.uns.ftn.svt.Service.FacilityService;
import rs.ac.uns.ftn.svt.Service.WorkDayService;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@RequiredArgsConstructor
@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final WorkDayService workDayService;
    private final DisciplineService disciplineService;
    private final ExcerciseRepository excerciseRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final ReviewRepository reviewRepository;
    private final ManagesRepository managesRepository;
    private final CommentRepository commentRepository;


    @Override
    public List<FacilityDto>getAll(){
        return facilityRepository.findAll().stream().map(FacilityDto::convertToDto).toList();
    }

    @Override
    public List<FacilityDto>getAllActive(){
        return facilityRepository.findAllByActiveTrue().stream().map(FacilityDto::convertToDto).toList();
    }


    @Override
    public FacilityDto getOneById(Long id) {
        return FacilityDto.convertToDto( facilityRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Facility doesn't exist!"))));
    }
    @Transactional
    @Override
    public void delete(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facility with id " + id + " doesn't exist!"));

        excerciseRepository.deleteByFacility(facility);
        // Prvo briši povezane zapise u manages tabeli
        managesRepository.deleteByFacility(facility);

        // Briši sve povezane recenzije za ovaj objekat
        reviewRepository.deleteByFacility(facility);

        // Zatim briši facility
        facilityRepository.delete(facility);
    }


    @Override
    @Transactional
    public FacilityDto create(FacilityDto facilityDto) {
        Optional<Facility> existingFacility = facilityRepository.findByName(facilityDto.getName());
        if (existingFacility.isPresent()) {
            throw new BadRequestException("Facility with this name already exists.");
        }

        Facility facility = facilityDto.convertToModel();
        facility.setCreatedAt(LocalDateTime.now());
        facility.setActive(false);
        System.out.println(facility.isActive());
        Facility savedFacility = facilityRepository.save(facility);

        for (WorkDay workDay : facility.getWorkDays()) {
            workDay.setFacility(savedFacility);
            workDayService.save(workDay);
        }

        for (Discipline discipline : facility.getDisciplines()) {
            discipline.setFacility(savedFacility);
            disciplineService.save(discipline);
        }

        FacilityDto resultDto = FacilityDto.convertToDto(savedFacility);

        for (WorkDayDto workDayDto : resultDto.getWorkDays()) {
            workDayDto.setFacilityId(savedFacility.getId());
        }

        for (DisciplineDto disciplineDto : resultDto.getDisciplines()) {
            disciplineDto.setFacilityId(savedFacility.getId());
        }
        saveManages(resultDto);

        return resultDto;
    }

    private void saveManages(FacilityDto facilityDto) {
        // Dobavljanje trenutnog ulogovanog korisnika pomoću Spring Security-a
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // Pretpostavljamo da je korisničko ime email
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new NotFoundException("Logged-in user not found"));

        ManagesDto managesDto = ManagesDto.builder()
                .user(null)
                .facility(facilityDto)
                .startDate(LocalDate.now()) // Postavi trenutni datum kao startDate
                .build();

        // Sačuvaj u Manages tabeli
        managesRepository.save(managesDto.convertToModel());
    }

    @Override
    public List<FacilityDto> searchByCity(String city) {
        return facilityRepository.findAll().stream()
                .filter(facility -> facility.getCity().equalsIgnoreCase(city))
                .map(FacilityDto::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<FacilityDto> searchByDiscipline(String discipline) {
        return facilityRepository.findAll().stream()
                .filter(facility -> facility.getDisciplines().stream().anyMatch(d -> d.getName().equalsIgnoreCase(discipline)))
                .map(FacilityDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDto> searchByRatingRange(Double minRating, Double maxRating) {
        return facilityRepository.findAll().stream()
                .filter(facility -> facility.getTotalRating() != null &&
                        facility.getTotalRating() >= minRating && facility.getTotalRating() <= maxRating)
                .map(FacilityDto::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<FacilityDto> searchByWorkDay(String workDay) {
        return facilityRepository.findAll().stream()
                .filter(facility -> facility.getWorkDays().stream().anyMatch(wd -> wd.getDay().name().equalsIgnoreCase(workDay)))
                .map(FacilityDto::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<FacilityDto> searchByWorkTime(String day, String fromTime, String untilTime) {
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
        LocalTime from = LocalTime.parse(fromTime);
        LocalTime until = LocalTime.parse(untilTime);

        return facilityRepository.findAll().stream()
                .filter(facility -> facility.getWorkDays().stream()
                        .anyMatch(workDay -> workDay.getDay().equals(dayOfWeek) &&
                                !workDay.getFromTime().isAfter(from) &&
                                !workDay.getUntilTime().isBefore(until)))
                .map(FacilityDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FacilityDto update(Long id, FacilityDto facilityDto) {
        Facility existingFacility = facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Facility with id " + id + " doesn't exist!"));

        // Update fields of the existing facility
        existingFacility.setName(facilityDto.getName());
        existingFacility.setDescription(facilityDto.getDescription());
        existingFacility.setAddress(facilityDto.getAddress());
        existingFacility.setCity(facilityDto.getCity());
        existingFacility.setTotalRating(facilityDto.getTotalRating());
        existingFacility.setActive(facilityDto.isActive());

        // Update work days and disciplines (if they need to be updated)
        // This logic depends on how you want to handle updates to related entities
        // For simplicity, let's assume we replace existing with new ones
        if (facilityDto.getWorkDays() != null) {
            existingFacility.getWorkDays().clear();
            facilityDto.getWorkDays().forEach(workDayDto -> {
                WorkDay workDay = workDayDto.convertToModel();
                workDay.setFacility(existingFacility);
                existingFacility.getWorkDays().add(workDay);
                workDayService.save(workDay);
            });
        }

        if (facilityDto.getDisciplines() != null) {
            existingFacility.getDisciplines().clear();
            facilityDto.getDisciplines().forEach(disciplineDto -> {
                Discipline discipline = disciplineDto.convertToModel();
                discipline.setFacility(existingFacility);
                existingFacility.getDisciplines().add(discipline);
                disciplineService.save(discipline);
            });
        }

        Facility updatedFacility = facilityRepository.save(existingFacility);
        return FacilityDto.convertToDto(updatedFacility);
    }

    @Transactional
    @Override
    public ExcerciseDto createExcercise(ExcerciseDto excerciseDto, Principal principal) {
        // Fetch the currently logged-in user based on the principal
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        // Fetch the facility
        Facility facility = facilityRepository.findById(excerciseDto.getFacilityId())
                .orElseThrow(() -> new BadRequestException("Facility not found!"));

        // Calculate untilDateTime based on duration
        LocalDateTime fromDateTime = excerciseDto.getFromDateTime();
        LocalDateTime untilDateTime = fromDateTime.plusMinutes(excerciseDto.getDuration());

        // Validate time within work hours
        validateTimeWithinWorkHours(fromDateTime, untilDateTime, facility);

        // Check if the time slot is available
        boolean isSlotTaken = excerciseRepository.existsByFacilityAndFromDateTimeBetweenOrUntilDateTimeBetween(
                facility,
                fromDateTime,
                fromDateTime,
                untilDateTime,
                untilDateTime
        );

        if (isSlotTaken) {
            throw new BadRequestException("Time slot is already taken!");
        }

        // Convert DTO to model and save
        Excercise excercise = excerciseDto.convertToModel();
        excercise.setFacility(facility);
        excercise.setUser(user);  // Koristi korisnika iz principal-a
        excercise.setUntilDateTime(untilDateTime);

        Excercise savedExcercise = excerciseRepository.save(excercise);
        return ExcerciseDto.convertToDto(savedExcercise);
    }

    private void validateTimeWithinWorkHours(LocalDateTime fromDateTime, LocalDateTime untilDateTime, Facility facility) {
        DayOfWeek dayOfWeek = fromDateTime.getDayOfWeek();
        Optional<WorkDay> workDayOptional = facility.getWorkDays().stream()
                .filter(workDay -> workDay.getDay().equals(dayOfWeek))
                .findFirst();

        if (workDayOptional.isEmpty()) {
            throw new BadRequestException("Facility is closed on this day!");
        }

        WorkDay workDay = workDayOptional.get();
        LocalTime fromTime = fromDateTime.toLocalTime();
        LocalTime untilTime = untilDateTime.toLocalTime();

        if (fromTime.isBefore(workDay.getFromTime()) || untilTime.isAfter(workDay.getUntilTime())) {
            throw new BadRequestException("Time slot is outside of work hours!");
        }
    }

    @Transactional
    @Override
    public ReviewDto createReview(ReviewDto reviewDto, Principal principal) {
        // Izvuci email iz JWT tokena
        String email = principal.getName(); // Dobija email iz tokena
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Kreiranje Rate entiteta
        Rate rate = new Rate();
        rate.setEquipment(reviewDto.getRate().getEquipment());
        rate.setStaff(reviewDto.getRate().getStaff());
        rate.setHygene(reviewDto.getRate().getHygene());
        rate.setSpace(reviewDto.getRate().getSpace());

        rate = rateRepository.save(rate); // Čuvanje Rate entiteta

        // Kreiranje Review entiteta
        Review review = new Review();
        review.setRate(rate);
        review.setUser(user); // Postavljanje trenutno ulogovanog korisnika
        review.setFacility(facilityRepository.findById(reviewDto.getFacilityId())
                .orElseThrow(() -> new NotFoundException("Facility not found")));
        review.setCreatedAt(LocalDateTime.now());
        review.setHidden(false); // Podrazumevano hidden je false
        review.setExcerciseCount(reviewDto.getExcerciseCount());

        review = reviewRepository.save(review); // Čuvanje Review entiteta

        // Ažuriranje totalRating za Facility
        updateFacilityRating(review.getFacility());

        return ReviewDto.convertToDto(review);
    }


    private void updateFacilityRating(Facility facility) {
        // Dohvati sve recenzije za dati objekat (Facility) koje nisu skrivene
        List<Review> reviews = reviewRepository.findByFacility(facility).stream()
                .filter(review -> !review.getHidden()) // Isključi skrivene recenzije
                .collect(Collectors.toList());

        if (reviews.isEmpty()) {
            facility.setTotalRating(0.0);
            facilityRepository.save(facility);
            return;
        }

        // Mapa za grupisanje recenzija po kombinaciji facilityId i rateId
        Map<Long, List<Review>> reviewsByRateId = reviews.stream()
                .collect(Collectors.groupingBy(review -> review.getRate().getId()));

        double totalRatingSum = 0.0;
        int validRateCount = 0;

        for (Map.Entry<Long, List<Review>> entry : reviewsByRateId.entrySet()) {
            List<Review> rateReviews = entry.getValue();
            int count = rateReviews.size();

            // Računanje proseka za svaki atribut (equipment, staff, hygene, space) za ovaj rateId
            double equipmentSum = 0.0;
            double staffSum = 0.0;
            double hygeneSum = 0.0;
            double spaceSum = 0.0;

            for (Review review : rateReviews) {
                Rate rate = review.getRate();
                equipmentSum += rate.getEquipment();
                staffSum += rate.getStaff();
                hygeneSum += rate.getHygene();
                spaceSum += rate.getSpace();
            }

            double averageEquipment = equipmentSum / count;
            double averageStaff = staffSum / count;
            double averageHygene = hygeneSum / count;
            double averageSpace = spaceSum / count;

            // Prosečna ocena za trenutni rateId
            double rateAverage = (averageEquipment + averageStaff + averageHygene + averageSpace) / 4.0;
            totalRatingSum += rateAverage;
            validRateCount++;
        }

        // Ukupni prosečan `totalRating` za objekat
        double facilityAverageRating = validRateCount > 0 ? totalRatingSum / validRateCount : 0.0;
        facility.setTotalRating(facilityAverageRating);
        facilityRepository.save(facility);
    }

    @Transactional
    @Override
    public void assignManager(Long facilityId, Long userId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility not found with id: " + facilityId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        List<Manages> existingManagement = managesRepository.findByFacility(facility);

        if (existingManagement.isEmpty()) {
            // Ako nema menadžera, dodeli novog
            Manages newManage = Manages.builder()
                    .user(user)
                    .facility(facility)
                    .startDate(LocalDate.now())
                    .build();
            managesRepository.save(newManage);

            // Postavi facility na aktivan jer je menadžer sada dodeljen
            facility.setActive(true);
            facilityRepository.save(facility);

        } else {
            // Ako menadžer već postoji, dodaj još jednog menadžera
            Manages newManage = Manages.builder()
                    .user(user)
                    .facility(facility)
                    .startDate(LocalDate.now())
                    .build();
            managesRepository.save(newManage);
        }
    }

    @Override
    public List<UserDto> getManagersByFacilityId(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility with id " + facilityId + " not found"));

        List<Manages> managesList = managesRepository.findByFacility(facility);

        return managesList.stream()
                .map(manages -> UserDto.convertToDto(manages.getUser())) // Assuming UserDto has a static conversion method
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void removeManager(Long facilityId, Long userId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility not found with id: " + facilityId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Pronađi menadžerski zapis po facility i user
        Manages manages = managesRepository.findByUserAndFacility(user, facility)
                .orElseThrow(() -> new NotFoundException("Manager not found for this facility"));

        // Ukloni zapis menadžera
        managesRepository.delete(manages);

        // Proveri da li objekat sada nema menadžera
        List<Manages> remainingManagers = managesRepository.findByFacility(facility);
        if (remainingManagers.isEmpty()) {
            // Ako nema menadžera, deaktiviraj facility
            facility.setActive(false);
            facilityRepository.save(facility);
        }
    }

    @Override
    public List<ExcerciseDto> getExcerciseHistoryByUserId(Long userId) {
        // Proveri da li korisnik postoji
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Dohvati sve vežbe za korisnika
        List<Excercise> excercises = excerciseRepository.findByUser(user);

        // Konvertuj ih u DTO i vrati
        return excercises.stream()
                .map(ExcerciseDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ReviewDto> getAllReviewsForManagerFacility(Long userId, Long facilityId) {
        // Proveri da li korisnik postoji
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Proveri da li korisnik upravlja datim objektom
        boolean isManager = managesRepository.existsByUserIdAndFacilityId(userId, facilityId);
        if (!isManager) {
            throw new UnauthorizedException("User is not the manager of this facility");
        }

        // Dohvati sve recenzije za taj objekat
        List<Review> reviews = reviewRepository.findByFacilityId(facilityId);

        // Konvertuj recenzije u DTO i vrati
        return reviews.stream()
                .map(ReviewDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("Review with id " + reviewId + " not found.");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        if (review.getRate() != null) {
            rateRepository.deleteById(review.getRate().getId());
        }

        commentRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
        Facility facility = review.getFacility();
        updateFacilityRating(facility);
    }


}
