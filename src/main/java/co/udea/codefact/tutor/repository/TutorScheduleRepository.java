package co.udea.codefact.tutor.repository;

import java.time.LocalTime;
import java.util.List;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorScheduleRepository extends JpaRepository<TutorSchedule, Long> {


    List<TutorSchedule> findByTutor(Tutor tutor);
    List<TutorSchedule> findByTutorId(Long tutorId);

    void deleteByTutor(Tutor tutor);


    @Query(value = "SELECT COUNT(s) > 0 FROM tutor_schedule s " +
            "WHERE s.id_tutor = :tutorId " +
            "AND s.day = :day " +
            "AND (:startTime < s.end_time AND :endTime > s.start_time)", nativeQuery = true)
    boolean existsOverlappingSchedule(@Param("tutorId") Long tutorId,
                                      @Param("day") String day,
                                      @Param("startTime") LocalTime startTime,
                                      @Param("endTime") LocalTime endTime);

}
