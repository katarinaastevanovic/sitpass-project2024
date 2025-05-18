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
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "equipment")
    private Integer equipment;

    @Column(name = "staff")
    private Integer staff;

    @Column(name = "hygene")
    private Integer hygene;

    @Column(name = "space")
    private Integer space;
}
