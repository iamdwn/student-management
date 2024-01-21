package dozun.mini.services;

import dozun.mini.constants.Action;
import dozun.mini.constants.ResponseStatus;
import dozun.mini.constants.SubjectStatus;
import dozun.mini.dtos.SubjectDTO;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.model.ResponseObject;
import dozun.mini.repositories.ScoreRepository;
import dozun.mini.repositories.SubjectRepository;
import dozun.mini.utils.DTOConverter;
import dozun.mini.utils.SubjectModelConverter;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectService {

    private SubjectRepository subjectRepository;
    private ScoreRepository scoreRepository;
    private DTOConverter dtoConverter;
    private SubjectModelConverter smConverter;
    private ResponseStatus status;
    private String message;
    private Object data;

    public SubjectService(SubjectRepository subjectRepository, ScoreRepository scoreRepository, DTOConverter dtoConverter, SubjectModelConverter smConverter) {
        this.subjectRepository = subjectRepository;
        this.scoreRepository = scoreRepository;
        this.dtoConverter = dtoConverter;
        this.smConverter = smConverter;
    }

    public ResponseEntity<ResponseObject> viewSubject() {
        List<SubjectDTO> subjectList = subjectRepository.getAllBySubjectStatusIsTrue()
                .stream()
                .map(subjectEntity -> dtoConverter.toSubjectDTO(subjectEntity))
                .collect(Collectors.toList()
                );

        status = subjectList == null
                ? ResponseStatus.NOT_FOUND
                : ResponseStatus.FOUND;

        message = subjectList == null
                ? "list is empty"
                : "list found";

        data = subjectList == null ? null : subjectList;

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> createSubject(SubjectDTO newSubject) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(newSubject.getSubjectCode());

        status = smConverter.toStatus(Action.CREATE.toString(), subjectEntity, newSubject);

        message = smConverter.toMessage(Action.CREATE.toString(), subjectEntity, newSubject);

        data = status != ResponseStatus.SUCCESS ? ""
                : subjectRepository.save(
                new SubjectEntity(
                        newSubject.getSubjectCode(),
                        newSubject.getCredits(),
                        null,
                        null,
                        SubjectStatus.AVAILABLE
                ));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> updateSubject(SubjectDTO updatedSubject) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(updatedSubject.getSubjectCode());

        status = smConverter.toStatus(Action.UPDATE.toString(), subjectEntity, updatedSubject);

        message = smConverter.toMessage(Action.UPDATE.toString(), subjectEntity, updatedSubject);

        if (status != ResponseStatus.SUCCESS) data = "";
        else {
            subjectEntity.setSubjectCode(updatedSubject.getSubjectCode());
            subjectEntity.setCredits(updatedSubject.getCredits());
            data = subjectRepository.save(subjectEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> deleteSubject(SubjectDTO deletedSubject) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(deletedSubject.getSubjectCode());

        status = smConverter.toStatus(Action.DELETE.toString(), subjectEntity, deletedSubject);

        message = smConverter.toMessage(Action.DELETE.toString(), subjectEntity, deletedSubject);

        if (status != ResponseStatus.SUCCESS) data = "";
        else {
            scoreRepository.deleteAllBySubject(subjectEntity);
            subjectEntity.setSubjectStatus(SubjectStatus.UNAVAILABLE);
            data = subjectRepository.save(subjectEntity);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }

    public ResponseEntity<ResponseObject> searchSubject(SubjectDTO keyword) {
        SubjectEntity subjectEntity = subjectRepository.findBySubjectCode(keyword.getSubjectCode());

        status = smConverter.toStatus(Action.SEARCH.toString(), subjectEntity, keyword);

        message = smConverter.toMessage(Action.SEARCH.toString(), subjectEntity, keyword);

        data = status != ResponseStatus.SUCCESS
                ? ""
                : dtoConverter.toSubjectDTO(subjectEntity);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(status, message, data));
    }
}
