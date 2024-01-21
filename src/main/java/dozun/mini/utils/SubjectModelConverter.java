package dozun.mini.utils;

import dozun.mini.constants.ResponseStatus;
import dozun.mini.constants.SubjectStatus;
import dozun.mini.dtos.SubjectDTO;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.repositories.SubjectRepository;
import org.springframework.stereotype.Component;

@Component
public class SubjectModelConverter {

    private SubjectRepository subjectRepository;

    public SubjectModelConverter(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public ResponseStatus toStatus(String action, SubjectEntity subjectEntity, SubjectDTO subjectDTO) {
        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().name();

        return subjectDTO.getSubjectCode() == null
                || subjectDTO.getSubjectCode().isBlank()
                ? ResponseStatus.BAD_REQUEST
                : subjectEntity == null
                    ? action.equalsIgnoreCase("create")
                        ? ResponseStatus.SUCCESS
                        : ResponseStatus.BAD_REQUEST
                    : action.equalsIgnoreCase("create")
                        ? ResponseStatus.BAD_REQUEST
                        : subjectStatus.equalsIgnoreCase(SubjectStatus.UNAVAILABLE.toString())
                            ? ResponseStatus.BAD_REQUEST
                            : ResponseStatus.SUCCESS;
    }

    public String toMessage(String action, SubjectEntity subjectEntity, SubjectDTO subjectDTO) {
        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().name();

        if (subjectDTO.getSubjectCode() == null
                || subjectDTO.getSubjectCode().isBlank()) return "subject is not null";

        if (subjectStatus.equalsIgnoreCase(SubjectStatus.UNAVAILABLE.toString())) return "subject was unavailable";

        if (subjectEntity == null)
            switch (action) {
                case "CREATE":
                    return "created successfully";
                default:
                    return "not found this subject";
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
                    return "duplicated subject";
            }
    }
}
