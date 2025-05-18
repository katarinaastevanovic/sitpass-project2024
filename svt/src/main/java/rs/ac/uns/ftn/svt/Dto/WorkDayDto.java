package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.WorkDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WorkDayDto {

    private long id;
    private Long facilityId;  // Promenio na Long da može da bude null ako nema facility
    private LocalDate validFrom;
    private DayOfWeek day;
    private LocalTime fromTime;
    private LocalTime untilTime;


    public WorkDay convertToModel() {
        // Pretvorba DTO-a u model
        return WorkDay.builder()
                .id(getId())
              //  .facility(facilityId != null ? Facility.builder().id(facilityId).build() : null)
                .validFrom(getValidFrom())
                .day(getDay())
                .fromTime(getFromTime())
                .untilTime(getUntilTime())
                .build();
    }

    public static WorkDayDto convertToDto(WorkDay workDay) {
        // Pretvorba modela u DTO
        return WorkDayDto.builder()
                .id(workDay.getId())
                .facilityId(workDay.getFacility().getId())
                .validFrom(workDay.getValidFrom())
                .day(workDay.getDay())
                .fromTime(workDay.getFromTime())
                .untilTime(workDay.getUntilTime())
                .build();
    }
}