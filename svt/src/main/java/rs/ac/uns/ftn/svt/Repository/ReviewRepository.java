package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFacility(Facility facility);

    @Transactional
    void deleteByFacility(Facility facility);

    List<Review> findByFacilityId(Long facilityId);
}

