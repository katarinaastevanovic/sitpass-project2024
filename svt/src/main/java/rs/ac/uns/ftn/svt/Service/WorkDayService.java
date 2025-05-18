package rs.ac.uns.ftn.svt.Service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.Model.WorkDay;
import rs.ac.uns.ftn.svt.Repository.WorkDayRepository;

@Service
public interface WorkDayService {


    void save(WorkDay workDay);
}
