package rs.ac.uns.ftn.svt.Service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Model.Discipline;

@Service
public interface DisciplineService {
    void save(Discipline discipline);
}
