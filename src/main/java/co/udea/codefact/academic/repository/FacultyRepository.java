package co.udea.codefact.academic.repository;

import java.util.Optional;

import co.udea.codefact.academic.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByName(String name);
    
}
