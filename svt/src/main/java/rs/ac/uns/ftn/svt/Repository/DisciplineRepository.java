package rs.ac.uns.ftn.svt.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.svt.Model.Discipline;
import rs.ac.uns.ftn.svt.Model.User;

import java.util.Optional;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

}

