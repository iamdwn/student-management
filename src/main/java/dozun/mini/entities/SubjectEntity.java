package dozun.mini.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dozun.mini.constants.SubjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject")
@EntityListeners(AuditingEntityListener.class)
public class SubjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String subjectCode;

    @Column
    private int credits;

    @OneToMany(mappedBy = "subject")
    @JsonIgnore
    private List<ScoreEntity> scores;

    @ManyToMany(mappedBy = "subjects")
    @JsonIgnore
    private List<StudentEntity> students = new ArrayList<>();

    @Column
    @Enumerated(EnumType.ORDINAL)
    private SubjectStatus subjectStatus;

    public SubjectEntity(String subjectCode, int credits, List<ScoreEntity> scores, List<StudentEntity> students, SubjectStatus subjectStatus) {
        this.subjectCode = subjectCode;
        this.credits = credits;
        this.scores = scores;
        this.students = students;
        this.subjectStatus = subjectStatus;
    }
}
