package rs.ac.uns.ftn.svt.Dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}