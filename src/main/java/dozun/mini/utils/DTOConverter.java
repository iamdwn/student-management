package dozun.mini.utils;

import dozun.mini.dtos.ScoreDTO;
import dozun.mini.dtos.StudentDTO;
import dozun.mini.dtos.SubjectDTO;
import dozun.mini.entities.ScoreEntity;
import dozun.mini.entities.StudentEntity;
import dozun.mini.entities.SubjectEntity;
import dozun.mini.repositories.ScoreRepository;
import dozun.mini.repositories.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DTOConverter {

    private ScoreRepository scoreRepository;
    private StudentRepository studentRepository;

    public DTOConverter(ScoreRepository scoreRepository, StudentRepository studentRepository) {
        this.scoreRepository = scoreRepository;
        this.studentRepository = studentRepository;
    }

    public ScoreDTO toScoreDTO(ScoreEntity scoreEntity, Boolean isStudentEntity) {
        return !isStudentEntity
                ? new ScoreDTO(
                scoreEntity.getStudent().getRollNumber(),
                scoreEntity.getStudent().getName(),
                scoreEntity.getSubject().getSubjectCode(),
                scoreEntity.getScore())

                : new ScoreDTO(
                null,
                null,
                scoreEntity.getSubject().getSubjectCode(),
                scoreEntity.getScore()
        );

    }

    public StudentDTO toStudentDTO(StudentEntity studentEntity) {
        return new StudentDTO(
                studentEntity.getId(),
                studentEntity.getRollNumber(),
                studentEntity.getName(),
                studentEntity.getDateOfBirth(),
                studentEntity.getGender(),
                studentEntity.getEmail(),
                scoreRepository.findAllByRollNumberAndSubjectId(
                                studentEntity.getRollNumber(),
                                studentRepository.findSubjectIdByStudentRollNumber(studentEntity.getRollNumber())
                        )
                        .stream()
                        .map(scoreEntity -> toScoreDTO(scoreEntity, true))
                        .collect(Collectors.toList())
        );
    }

    public SubjectDTO toSubjectDTO(SubjectEntity subjectEntity) {
        return new SubjectDTO(
                subjectEntity.getSubjectCode(),
                subjectEntity.getCredits()
        );
    }

}
