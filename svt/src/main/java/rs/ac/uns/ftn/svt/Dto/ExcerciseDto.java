package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.Excercise;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.User;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExcerciseDto {

    private Long id;
    private Long userId;
    private Long facilityId;
    private LocalDateTime fromDateTime;
    private int duration;   // Trajanje u minutima
    private String facilityName;


    public static ExcerciseDto convertToDto(Excercise excercise) {
        return new ExcerciseDto(
                excercise.getId(),
                excercise.getUser().getId(),
                excercise.getFacility().getId(),
                excercise.getFromDateTime(),
                (int) Duration.between(excercise.getFromDateTime(), excercise.getUntilDateTime()).toMinutes(),
                excercise.getFacility().getName() // Dodavanje imena objekta
        );
    }

    public Excercise convertToModel() {
        // Provera da li su userId i facilityId null pre konverzije
        Facility facility = this.facilityId != null ? Facility.builder().id(this.facilityId).build() : null;
        User user = this.userId != null ? User.builder().id(this.userId).build() : null;

        return Excercise.builder()
                .id(this.id != null ? this.id : 0L)  // Postavljanje ID na 0 ako je null, pošto JPA tretira 0 kao nepostojeći ID
                .facility(facility)
                .user(user)
                .fromDateTime(this.fromDateTime)
                .untilDateTime(this.fromDateTime.plusMinutes(this.duration))
                .build();
    }

}
