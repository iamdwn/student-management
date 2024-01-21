package dozun.mini.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "score")
@EntityListeners(AuditingEntityListener.class)
public class ScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

        @ManyToOne
    //    @JsonIgnore
        @JoinColumn(name = "student_id")
        private StudentEntity student;

        @ManyToOne
    //    @JsonIgnore
        @JoinColumn(name = "subject_id")
        private SubjectEntity subject;

        @Column
        private Double score;

    public ScoreEntity(StudentEntity student, SubjectEntity subject, Double score) {
        this.student = student;
        this.subject = subject;
        this.score = score;
    }
}
