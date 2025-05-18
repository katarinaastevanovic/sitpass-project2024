package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.Facility;
import rs.ac.uns.ftn.svt.Model.Manages;
import rs.ac.uns.ftn.svt.Model.User;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ManagesDto {

    private long id;
    private UserDto user;
    private FacilityDto facility;
    private LocalDate startDate;
    private LocalDate endDate;

    // Konvertovanje iz DTO u model
    public Manages convertToModel() {
        return Manages.builder()
                .id(id)
                .user(user != null ? user.convertToModel() : null)// Pretpostavka da imaš metodu convertToModel u UserDto
                .facility(facility.convertToModel()) // Pretpostavka da već imaš metodu convertToModel u FacilityDto
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    // Konvertovanje iz modela u DTO
    public static ManagesDto convertToDto(Manages manages) {
        return ManagesDto.builder()
                .id(manages.getId())
                .user(UserDto.convertToDto(manages.getUser())) // Pretpostavka da imaš metodu convertToDto u UserDto
                .facility(FacilityDto.convertToDto(manages.getFacility())) // Pretpostavka da već imaš metodu convertToDto u FacilityDto
                .startDate(manages.getStartDate())
                .endDate(manages.getEndDate())
                .build();
    }
}
