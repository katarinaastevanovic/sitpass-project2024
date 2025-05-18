package rs.ac.uns.ftn.svt.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Model.Discipline;
import rs.ac.uns.ftn.svt.Repository.DisciplineRepository;
import rs.ac.uns.ftn.svt.Service.DisciplineService;

@RequiredArgsConstructor
@Service
public class DisciplineServiceImpl implements DisciplineService {
    private final DisciplineRepository disciplineRepository;
    @Override
    public void save(Discipline discipline) {
        disciplineRepository.save(discipline);
    }
}
