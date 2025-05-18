package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;
import rs.ac.uns.ftn.svt.Enum.RequestStatus;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "createdAt")
    private LocalDate createdAt;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "rejectionReason")
    private String rejectionReason;

    @Column(name = "password")
    private String password;
}
