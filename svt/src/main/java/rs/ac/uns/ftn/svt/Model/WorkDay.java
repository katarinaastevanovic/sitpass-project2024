package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "validFrom")
    private LocalDate validFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private DayOfWeek day;

    @Column(name = "fromTime")
    private LocalTime fromTime;

    @Column(name = "untilTime")
    private LocalTime untilTime;



}
