package co.udea.codefact.professor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByUserId(Long id);

    Optional<Professor> findTopByUserIdOrderByIdDesc(Long userId);

    List<Professor> findAllByUserId(Long id);
}
