package rs.ac.uns.ftn.svt.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Model.Excercise;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ExcerciseRepository extends JpaRepository<Excercise, Long> {

    boolean existsByFacilityAndFromDateTimeBetweenOrUntilDateTimeBetween(
            Facility facility,
            LocalDateTime fromStart,
            LocalDateTime fromEnd,
            LocalDateTime untilStart,
            LocalDateTime untilEnd
    );
    @Transactional
    void deleteByFacility(Facility facility);

    List<Excercise> findByUser(User user);
}
