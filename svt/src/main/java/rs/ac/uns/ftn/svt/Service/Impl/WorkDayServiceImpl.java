package rs.ac.uns.ftn.svt.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Model.WorkDay;
import rs.ac.uns.ftn.svt.Repository.WorkDayRepository;
import rs.ac.uns.ftn.svt.Service.WorkDayService;

@RequiredArgsConstructor
@Service
public class WorkDayServiceImpl implements WorkDayService {
    private final WorkDayRepository workDayRepository;
    @Override
    public void save(WorkDay workDay) {
        workDayRepository.save(workDay);
    }
}
