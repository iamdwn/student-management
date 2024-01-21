package dozun.mini.services;

import dozun.mini.constants.Action;
import dozun.mini.constants.ResponseStatus;
import dozun.mini.constants.StudentStatus;
import dozun.mini.dtos.StudentDTO;
import dozun.mini.entities.StudentEntity;
import dozun.mini.model.ResponseObject;
import dozun.mini.repositories.ScoreRepository;
import dozun.mini.repositories.StudentRepository;
import dozun.mini.utils.DTOConverter;
import dozun.mini.utils.StudentModelConverter;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;
    private ScoreRepository scoreRepository;
    private DTOConverter dtoConverter;
    private StudentModelConverter smConverter;
    private ResponseStatus status;
    private String message;
    private Object data;

    public StudentService(StudentRepository studentRepository, ScoreRepository scoreRepository, DTOConverter dtoConverter, StudentModelConverter smConverter) {
        this.studentRepository = studentRepository;
        this.scoreRepository = scoreRepository;
        this.dtoConverter = dtoConverter;
        this.smConverter = smConverter;
    }

    public ResponseEntity<ResponseObject> viewStudent() {
        List<StudentDTO> studentList = studentRepository.getAllByStudentStatusIsTrue()
                .stream()
                .map(studentEntity -> dtoConverter.toStudentDTO(studentEntity))
                .collect(Collectors.toList());

        status = studentList == null
                ? ResponseStatus.NOT_FOUND
                : ResponseStatus.FOUND;

        message = studentList == null
                ? "list is empty"
                : "list found";
        data = studentList == null
                ? null
                : studentList;

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> createStudent(StudentDTO newStudent) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(newStudent.getRollNumber());

        status = smConverter.toStatus(Action.CREATE.toString(), studentEntity, newStudent);

        message = smConverter.toMessage(Action.CREATE.toString(), studentEntity, newStudent);

        data = status != ResponseStatus.SUCCESS ? ""
                : studentRepository.save(
                new StudentEntity(
                        newStudent.getRollNumber(),
                        newStudent.getName(),
                        newStudent.getDateOfBirth(),
                        newStudent.getGender(),
                        newStudent.getEmail(),
                        null,
                        null,
                        StudentStatus.ACTIVE
                ));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> updateStudent(StudentDTO updatedStudent) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(updatedStudent.getRollNumber());

        status = smConverter.toStatus(Action.UPDATE.toString(), studentEntity, updatedStudent);

        message = smConverter.toMessage(Action.UPDATE.toString(), studentEntity, updatedStudent);

        if (status != ResponseStatus.SUCCESS) data = "";
        else {
            studentEntity.setRollNumber(updatedStudent.getRollNumber());
            studentEntity.setName(updatedStudent.getName());
            studentEntity.setDateOfBirth(updatedStudent.getDateOfBirth());
            studentEntity.setGender(updatedStudent.getGender());
            studentEntity.setEmail(updatedStudent.getEmail());
            data = studentRepository.save(studentEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> deleteStudent(StudentDTO deletedStudent) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(deletedStudent.getRollNumber());

        status = smConverter.toStatus(Action.DELETE.toString(), studentEntity, deletedStudent);

        message = smConverter.toMessage(Action.DELETE.toString(), studentEntity, deletedStudent);

        if (status != ResponseStatus.SUCCESS) data = "";
        else {
            studentEntity.setStudentStatus(StudentStatus.INACTIVE);
            scoreRepository.deleteAllByStudent(studentEntity);
            data = studentRepository.save(studentEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> searchStudent(StudentDTO keyword) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(keyword.getRollNumber());

        status = smConverter.toStatus(Action.SEARCH.toString(), studentEntity, keyword);

        message = smConverter.toMessage(Action.SEARCH.toString(), studentEntity, keyword);

        data = status != ResponseStatus.SUCCESS
                ? ""
                : dtoConverter.toStudentDTO(studentEntity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }
}
