package dozun.mini.utils;

import dozun.mini.constants.ResponseStatus;
import dozun.mini.constants.StudentStatus;
import dozun.mini.dtos.StudentDTO;
import dozun.mini.dtos.SubjectDTO;
import dozun.mini.entities.StudentEntity;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.repositories.StudentRepository;
import org.springframework.stereotype.Component;

@Component
public class StudentModelConverter {

    private StudentRepository studentRepository;

    public StudentModelConverter(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public ResponseStatus toStatus(String action, StudentEntity studentEntity, StudentDTO studentDTO) {
        String subjectStatus = studentEntity == null ? ""
                : studentEntity.getStudentStatus().name();

        return studentDTO.getRollNumber() == null
                || studentDTO.getRollNumber().isBlank()
                ? ResponseStatus.BAD_REQUEST
                : studentEntity == null
                    ? action.equalsIgnoreCase("create")
                        ? ResponseStatus.SUCCESS
                        : ResponseStatus.BAD_REQUEST
                    : action.equalsIgnoreCase("create")
                        ? ResponseStatus.BAD_REQUEST
                        : subjectStatus.equalsIgnoreCase(StudentStatus.INACTIVE.toString())
                            ? ResponseStatus.BAD_REQUEST
                            : ResponseStatus.SUCCESS;
    }

    public String toMessage(String action, StudentEntity studentEntity, StudentDTO studentDTO) {
        String studentStatus = studentEntity == null ? ""
                : studentEntity.getStudentStatus().name();

        if (studentDTO.getRollNumber() == null
                || studentDTO.getRollNumber().isBlank()) return "student is not null";

        if (studentStatus.equalsIgnoreCase(StudentStatus.INACTIVE.toString())) return "student was inactive";

        if (studentEntity == null)
            switch (action) {
                case "CREATE":
                    return "created successfully";
                default:
                    return "not found this student";
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
                    return "duplicated student";
            }
    }
}
