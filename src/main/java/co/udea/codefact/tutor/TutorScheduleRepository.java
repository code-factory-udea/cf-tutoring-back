package co.udea.codefact.tutor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorScheduleRepository extends JpaRepository<TutorSchedule, Long> {
    
    List<TutorSchedule> findByTutorId(Long tutorId);

    void deleteByTutor(Tutor tutor);
    
}
