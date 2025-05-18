package rs.ac.uns.ftn.svt.Dto;
import lombok.*;
import rs.ac.uns.ftn.svt.Model.Discipline;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DisciplineDto {

    private long id;
    private Long facilityId; // Promenjen na Long da može biti null ako nema facility
    private String name;


    public Discipline convertToModel() {
        // Pretvorba DTO-a u model
        return Discipline.builder()
                .id(getId())
                .name(getName())
               // .facility(getFacility().getId())
                .build();
    }

    public static DisciplineDto convertToDto(Discipline discipline) {
        // Pretvorba modela u DTO
        return DisciplineDto.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .facilityId(discipline.getFacility().getId())
                .build();
    }
}
