package dozun.mini.dtos;

import lombok.*;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
    private String subjectCode;
    private int credits;
}
