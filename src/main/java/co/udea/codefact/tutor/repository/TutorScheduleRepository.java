package co.udea.codefact.tutor.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorScheduleRepository extends JpaRepository<TutorSchedule, Long> {
    
    List<TutorSchedule> findByTutorId(Long tutorId);

    void deleteByTutor(Tutor tutor);

    boolean existsByTutorAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Tutor tutor,
            DayOfWeek day,
            LocalTime newEndTime,
            LocalTime newStartTime
    );
    
}
