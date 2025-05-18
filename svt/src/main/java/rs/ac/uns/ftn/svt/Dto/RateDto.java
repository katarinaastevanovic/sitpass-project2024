package rs.ac.uns.ftn.svt.Dto;

import lombok.Data;
import rs.ac.uns.ftn.svt.Model.Rate;

@Data
public class RateDto {
    private Integer equipment;
    private Integer staff;
    private Integer hygene;
    private Integer space;

    public Rate convertToModel() {
        return Rate.builder()
                .equipment(this.equipment)
                .staff(this.staff)
                .hygene(this.hygene)
                .space(this.space)
                .build();
    }

    public static RateDto convertToDto(Rate rate) {
        RateDto rateDto = new RateDto();
        rateDto.setEquipment(rate.getEquipment());
        rateDto.setStaff(rate.getStaff());
        rateDto.setHygene(rate.getHygene());
        rateDto.setSpace(rate.getSpace());
        return rateDto;
    }
}
