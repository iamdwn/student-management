package dozun.mini.repositories;

import dozun.mini.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> getAllByStudentStatusIsTrue();

    @Query("SELECT sub.id FROM StudentEntity stu " +
            "JOIN ScoreEntity sco ON stu.rollNumber = sco.student.rollNumber " +
            "JOIN SubjectEntity sub ON sub.id = sco.subject.id " +
            "WHERE stu.rollNumber = :rollNumber")
    List<Long> findSubjectIdByStudentRollNumber(@Param("rollNumber") String rollNumber);

    StudentEntity findByRollNumber(String rollNumber);
}
