package dozun.mini.repositories;

import dozun.mini.entities.ScoreEntity;
import dozun.mini.entities.StudentEntity;
import dozun.mini.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreEntity, Long> {

    @Query("SELECT sco FROM ScoreEntity sco " +
            "WHERE sco.student.rollNumber = :rollNumber AND sco.subject.id IN :subjectId")
    List<ScoreEntity> findAllByRollNumberAndSubjectId(@Param("rollNumber") String rollNumber,
                                                      @Param(("subjectId")) List<Long> subjectId);

    @Query("SELECT sco FROM ScoreEntity sco " +
            "WHERE sco.student.rollNumber = :rollNumber AND sco.subject.subjectCode = :subjectCode")
    ScoreEntity findByRollNumberAndSubjectCode(@Param("rollNumber") String rollNumber,
                                               @Param(("subjectCode")) String subjectCode);

    @Query("SELECT sco FROM ScoreEntity sco " +
            "JOIN StudentEntity stu ON sco.student.rollNumber = stu.rollNumber AND stu.studentStatus = 1" +
            "JOIN SubjectEntity sub ON sco.subject.subjectCode = sub.subjectCode AND sub.subjectStatus = 1" +
            "WHERE sco.subject.subjectCode = :subjectCode")
    List<ScoreEntity> findBySubjectCode(@Param(("subjectCode")) String subjectCode);

    void deleteAllByStudent(StudentEntity student);

    void deleteAllBySubject(SubjectEntity subject);
}
