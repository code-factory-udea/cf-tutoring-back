package co.udea.codefact.tutor.repository;

import java.util.List;
import java.util.Optional;

import co.udea.codefact.tutor.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByUserId(Long userId);
    
    Optional<Tutor> findByUserUsername(String username);

    @Query("SELECT t FROM Tutor t WHERE t.isActive = true AND t.subject.code = :subjectCode")
    List<Tutor> findAllBySubjectCodeAndActivate(@Param("subjectCode") Long subjectId);
}
