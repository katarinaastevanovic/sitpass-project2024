package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Excercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "fromDateTime")
    private LocalDateTime fromDateTime;

    @Column(name = "untilDateTime")
    private LocalDateTime untilDateTime;


}
