package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.Manages;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Model.User;

import java.util.List;
import java.util.Optional;

public interface ManagesRepository extends JpaRepository<Manages, Long> {
    @Transactional
    void deleteByFacility(Facility facility);

    List<Manages> findByFacility(Facility facility);

    boolean existsByUserId(Long userId);

    boolean existsByUserAndFacility(User user, Facility facilityNotFound);

    Optional<Manages> findByUserAndFacility(User user, Facility facility);

    List<Manages> findByUserId(Long userId);

    boolean existsByUserIdAndFacilityId(Long userId, Long facilityId);
}
