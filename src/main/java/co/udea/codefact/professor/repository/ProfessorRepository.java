package co.udea.codefact.professor.repository;

import java.util.List;
import java.util.Optional;

import co.udea.codefact.professor.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByUserId(Long id);

    Optional<Professor> findFirstByUserUsername(String username);

    Optional<Professor> findTopByUserIdOrderByIdDesc(Long userId);

    boolean existsByUserUsernameAndSubjectId(String username, Long subjectId);

    List<Professor> findAllByUserId(Long id);


}
