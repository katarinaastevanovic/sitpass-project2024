package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Model.Facility;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findByName(String name);

    List<Facility> findAllByActiveTrue();

}