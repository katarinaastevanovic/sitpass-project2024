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
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Veza sa User klasom
    private User user;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "path")
    private String path;
}
