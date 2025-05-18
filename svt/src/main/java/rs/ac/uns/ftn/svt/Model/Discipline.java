package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "name")
    private String name;


}
