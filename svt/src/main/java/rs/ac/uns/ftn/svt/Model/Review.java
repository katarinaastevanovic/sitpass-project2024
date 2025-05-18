package rs.ac.uns.ftn.svt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "review")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "rate_id")
    private Rate rate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "excerciseCount")
    private Integer excerciseCount;

    @Column(name = "hidden")
    private Boolean hidden;
}
