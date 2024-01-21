package dozun.mini.repositories;

import dozun.mini.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> getAllBySubjectStatusIsTrue();

    @Query("SELECT sub.subjectCode FROM SubjectEntity sub WHERE sub.id = :subjectId")
    Optional<SubjectEntity> getSubjectCodeById(@Param("subjectId") Long subjectId);

    SubjectEntity findBySubjectCode(String subjectCode);
}
