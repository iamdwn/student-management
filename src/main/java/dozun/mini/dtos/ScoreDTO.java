package dozun.mini.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoreDTO {
    private String rollNumber;
    private String studentName;
    private String subjectCode;
    private Double score;

    public ScoreDTO(String rollNumber, String subjectCode) {
        this.rollNumber = rollNumber;
        this.subjectCode = subjectCode;
    }
}
