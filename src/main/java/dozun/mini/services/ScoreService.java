package dozun.mini.services;

import dozun.mini.constants.Action;
import dozun.mini.constants.ResponseStatus;
import dozun.mini.dtos.ScoreDTO;
import dozun.mini.entities.ScoreEntity;
import dozun.mini.entities.StudentEntity;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.model.ResponseObject;
import dozun.mini.repositories.ScoreRepository;
import dozun.mini.repositories.StudentRepository;
import dozun.mini.repositories.SubjectRepository;
import dozun.mini.utils.DTOConverter;
import dozun.mini.utils.ScoreModelConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    private ScoreRepository scoreRepository;
    private StudentRepository studentRepository;
    private SubjectRepository subjectRepository;
    private DTOConverter dtoConverter;
    private ScoreModelConverter smConverter;
    private ResponseStatus status;
    private String message;
    private Object data;

    public ScoreService(ScoreRepository scoreRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, DTOConverter dtoConverter, ScoreModelConverter smConverter) {
        this.scoreRepository = scoreRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.dtoConverter = dtoConverter;
        this.smConverter = smConverter;
    }

    public ResponseEntity<ResponseObject> viewScore() {
        List<ScoreDTO> scoreList = scoreRepository.findAll()
                .stream()
                .map(scoreEntity -> dtoConverter.toScoreDTO(scoreEntity, false))
                .collect(Collectors.toList());

        status = scoreList == null
                ? ResponseStatus.NOT_FOUND
                : ResponseStatus.FOUND;

        message = scoreList == null
                ? "list is empty"
                : "list found";
        data = scoreList == null

                ? null
                : scoreList;

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> createScore(ScoreDTO newScore) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(newScore.getRollNumber());
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(newScore.getSubjectCode());
        ScoreEntity scoreEntity = scoreRepository.findByRollNumberAndSubjectCode(newScore.getRollNumber(), newScore.getSubjectCode());
        String studentStatus = studentEntity == null ? ""
                : studentEntity.getStudentStatus().toString();
        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().toString();

        status = smConverter.toStatus(Action.CREATE.toString(), scoreEntity, studentEntity, subjectEntity, newScore);

        message = smConverter.toMessage(Action.CREATE.toString(), scoreEntity, studentEntity, subjectEntity, newScore);

        data = status != ResponseStatus.SUCCESS
                || studentStatus.equals("INACTIVE")
                || subjectStatus.equals("UNAVAILABLE")
                || scoreEntity != null
                ? ""
                : scoreRepository.save(
                new ScoreEntity(
                        studentEntity,
                        subjectEntity,
                        newScore.getScore()
                ));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> updateScore(ScoreDTO updatedScore) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(updatedScore.getRollNumber());
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(updatedScore.getSubjectCode());
        ScoreEntity scoreEntity = scoreRepository.findByRollNumberAndSubjectCode(updatedScore.getRollNumber(), updatedScore.getSubjectCode());

        status = smConverter.toStatus(Action.UPDATE.toString(), scoreEntity, studentEntity, subjectEntity, updatedScore);

        message = smConverter.toMessage(Action.UPDATE.toString(), scoreEntity, studentEntity, subjectEntity, updatedScore);

        if (status != ResponseStatus.SUCCESS) data = "";
        else {
            scoreEntity.setScore(updatedScore.getScore());
            data = scoreRepository.save(scoreEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> deleteScore(ScoreDTO deletedScore) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(deletedScore.getRollNumber());
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(deletedScore.getSubjectCode());
        ScoreEntity scoreEntity = scoreRepository.findByRollNumberAndSubjectCode(deletedScore.getRollNumber(), deletedScore.getSubjectCode());

        status = smConverter.toStatus(Action.DELETE.toString(), scoreEntity, studentEntity, subjectEntity, deletedScore);

        message = smConverter.toMessage(Action.DELETE.toString(), scoreEntity, studentEntity, subjectEntity, deletedScore);

        data = "";

        if (status == ResponseStatus.SUCCESS)
            scoreRepository.deleteById(scoreEntity.getId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> searchScore(ScoreDTO keyword) {
        StudentEntity studentEntity = studentRepository.findByRollNumber(keyword.getRollNumber());
        String studentStatus = studentEntity == null ? ""
                : studentEntity.getStudentStatus().toString();
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(keyword.getSubjectCode());
        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().toString();
        ScoreEntity scoreEntity = scoreRepository.findByRollNumberAndSubjectCode(keyword.getRollNumber(), keyword.getSubjectCode());

        status = smConverter.toStatus(Action.SEARCH.toString(), scoreEntity, studentEntity, subjectEntity, keyword);

        message = smConverter.toMessage(Action.SEARCH.toString(), scoreEntity, studentEntity, subjectEntity, keyword);

        data = status != ResponseStatus.SUCCESS
                ? ""
                : dtoConverter.toScoreDTO(scoreEntity, false);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> searchBySubjectCode(String keyword) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(keyword);
        List<ScoreDTO> scoreEntity = scoreRepository.findBySubjectCode(keyword)
                .stream()
                .map(entity -> dtoConverter.toScoreDTO(entity, false))
                .collect(Collectors.toList());
        String subjectStatus = subjectEntity == null ? ""
                : subjectEntity.getSubjectStatus().toString();

        status = keyword == null
                || subjectEntity == null
                || scoreEntity == null
                ? ResponseStatus.BAD_REQUEST
                : ResponseStatus.SUCCESS;

        message = keyword == null
                ? "subject is not null"
                : subjectEntity == null
                ? "not found this subject"
                : subjectStatus.equals("UNAVAILABLE")
                ? "subject was unavailable"
                : "searched successfully";

        data = status != ResponseStatus.SUCCESS
                ? ""
                : scoreEntity;

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }
}
