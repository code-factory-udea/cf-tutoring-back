package co.udea.codefact.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByCode(Long code);
    
    List<Subject> findAllByAcademicProgramId(Long academicProgramId);
}
