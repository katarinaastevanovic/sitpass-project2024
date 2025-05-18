package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Manages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "startDate")
    private LocalDate startDate;


    @Column(name = "endDate")
    private LocalDate endDate;


}
