package co.udea.codefact.academic.repository;

import co.udea.codefact.academic.entity.AcademicProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram, Long> {
    
}
