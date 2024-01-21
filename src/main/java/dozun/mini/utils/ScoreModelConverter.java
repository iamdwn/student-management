package dozun.mini.utils;

import dozun.mini.constants.ResponseStatus;
import dozun.mini.constants.StudentStatus;
import dozun.mini.constants.SubjectStatus;
import dozun.mini.dtos.ScoreDTO;
import dozun.mini.entities.ScoreEntity;
import dozun.mini.entities.StudentEntity;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.repositories.ScoreRepository;
import org.springframework.stereotype.Component;

@Component
public class ScoreModelConverter {

    private ScoreRepository scoreRepository;

    public ScoreModelConverter(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public ResponseStatus toStatus(String action, ScoreEntity scoreEntity, StudentEntity studentEntity, SubjectEntity subjectEntity, ScoreDTO scoreDTO) {

        return scoreDTO.getRollNumber() == null
                || scoreDTO.getRollNumber().isBlank()
                || scoreDTO.getSubjectCode() == null
                || scoreDTO.getSubjectCode().isBlank()
                ? ResponseStatus.BAD_REQUEST
                : studentEntity == null
                || subjectEntity == null
                ? ResponseStatus.BAD_REQUEST
                : scoreEntity == null
                ? action.equalsIgnoreCase("create")
                ? ResponseStatus.SUCCESS
                : ResponseStatus.BAD_REQUEST
                : action.equalsIgnoreCase("create")
                ? ResponseStatus.BAD_REQUEST
                : ResponseStatus.SUCCESS;

    }

    public String toMessage(String action, ScoreEntity scoreEntity, StudentEntity studentEntity, SubjectEntity subjectEntity, ScoreDTO scoreDTO) {
        String studentStatus = studentEntity == null ? ""
                : studentEntity.getStudentStatus().name();

        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().name();

        if (scoreDTO.getRollNumber() == null
                || scoreDTO.getRollNumber().isBlank()) return "student is not null";

        if (scoreDTO.getSubjectCode() == null
                || scoreDTO.getSubjectCode().isBlank()) return "subject is not null";

        if (action.equalsIgnoreCase("create")) {

            if (studentEntity == null) return "not found this student";
            if (subjectEntity == null) return "not found this subject";

            if (studentStatus.equalsIgnoreCase(StudentStatus.INACTIVE.toString())) return "student was inactive";
            if (subjectStatus.equalsIgnoreCase(SubjectStatus.UNAVAILABLE.toString())) return "subject was unavailable";
        }

        if (scoreEntity == null)
            switch (action) {
                case "CREATE":
                    return "created successfully";
                default:
                    return "not found this score of this student with this subject code";
            }

        else
            switch (action) {
                case "UPDATE":
                    return "updated successfully";
                case "DELETE":
                    return "deleted successfully";
                case "SEARCH":
                    return "searched successfully";
                default:
                    return "this student had score with this subject code";
            }
    }
}
