package rs.ac.uns.ftn.svt.Dto;

import lombok.*;
import rs.ac.uns.ftn.svt.Model.User;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    private long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;
    private LocalDate birthday;
    private LocalDate createdAt;
    private String zipCode;
    private String city;

    public User convertToModel() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .surname(surname)
                .phoneNumber(phoneNumber)
                .address(address)
                .birthday(birthday)
                .createdAt(createdAt)
                .zipCode(zipCode)
                .city(city)
                .build();
    }

    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .birthday(user.getBirthday())
                .createdAt(user.getCreatedAt())
                .zipCode(user.getZipCode())
                .city(user.getCity())
                .build();
    }
}
