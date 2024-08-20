package co.udea.codefact.tutor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByUserId(Long userId);
    
    Optional<Tutor> findByUserUsername(String username);
}
