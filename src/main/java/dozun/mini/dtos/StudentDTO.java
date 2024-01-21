package dozun.mini.dtos;

import dozun.mini.constants.Gender;
import lombok.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long studentId;
    private String rollNumber;
    private String name;
    private Date dateOfBirth;
    private Gender gender;
    private String email;
    private List<ScoreDTO> scoreList;

    public StudentDTO(String rollNumber, String name, Date dateOfBirth, Gender gender, String email, List<ScoreDTO> scoreList) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.scoreList = scoreList;
    }
}
