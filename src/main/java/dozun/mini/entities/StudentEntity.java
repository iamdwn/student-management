package dozun.mini.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dozun.mini.constants.Gender;
import dozun.mini.constants.StudentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
@EntityListeners(AuditingEntityListener.class)
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String rollNumber;

    @Column
    private String name;

    @Column
    private Date dateOfBirth;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column
    private String email;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<ScoreEntity> scores;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "score",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
        private List<SubjectEntity> subjects = new ArrayList<>();

    @Column
    @Enumerated(EnumType.ORDINAL)
    private StudentStatus studentStatus;

    public StudentEntity(String rollNumber, String name, Date dateOfBirth, Gender gender, String email, List<ScoreEntity> scores, List<SubjectEntity> subjects, StudentStatus studentStatus) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.scores = scores;
        this.subjects = subjects;
        this.studentStatus = studentStatus;
    }
}

