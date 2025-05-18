package rs.ac.uns.ftn.svt.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.svt.Enum.RequestStatus;
import rs.ac.uns.ftn.svt.Model.AccountRequest;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {
    private long id;
    private String email;
    private String address;
    private String password;
    private String rejectionReason;
    private RequestStatus status;
    private LocalDate createdAt;

    public AccountRequest convertToModel() {
        return AccountRequest.builder()
                .id(getId())
                .email(getEmail())
                .address(getAddress())
                .password(getPassword())
                .rejectionReason(getRejectionReason())
                .status(getStatus())
                .createdAt(getCreatedAt())
                .build();
    }

    public static AccountRequestDto convertToDto(AccountRequest accountRequest) {
        return AccountRequestDto.builder()
                .id(accountRequest.getId())
                .email(accountRequest.getEmail())
                .address(accountRequest.getAddress())
                .password(accountRequest.getPassword())
                .rejectionReason(accountRequest.getRejectionReason())
                .status(accountRequest.getStatus())
                .createdAt(accountRequest.getCreatedAt())
                .build();
    }
}
