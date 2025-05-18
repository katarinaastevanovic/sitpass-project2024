package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.Discipline;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.WorkDay;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FacilityDto {

    private long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private String address;
    private String city;
    private Double totalRating;
    private boolean active;
    private List<WorkDayDto>workDays;
    private List<DisciplineDto>disciplines;


    public Facility convertToModel(){
        return Facility.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .createdAt(getCreatedAt())
                .address(getAddress())
                .city(getCity())
                .totalRating(getTotalRating())
                .active(isActive())
                .workDays(getWorkDays().stream().map(WorkDayDto::convertToModel).toList())
                .disciplines(getDisciplines().stream().map(DisciplineDto::convertToModel).toList())
                .build();

    }

    public static FacilityDto convertToDto(Facility facility){
        return FacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .description(facility.getDescription())
                .createdAt(facility.getCreatedAt())
                .address(facility.getAddress())
                .city(facility.getCity())
                .totalRating(facility.getTotalRating())
                .active(facility.isActive())
                .workDays(facility.getWorkDays().stream().map(WorkDayDto::convertToDto).toList())
                .disciplines(facility.getDisciplines().stream().map(DisciplineDto::convertToDto).toList())
                .build();

    }


}
