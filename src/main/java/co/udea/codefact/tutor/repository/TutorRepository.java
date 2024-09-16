package co.udea.codefact.tutor.repository;

import java.util.Optional;

import co.udea.codefact.tutor.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByUserId(Long userId);
    
    Optional<Tutor> findByUserUsername(String username);
}
