package co.udea.codefact.appointment.repository;

import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByTutorAndStatus(Tutor tutor, AppointmentStatus status);
    List<Appointment> findAllByStudentAndStatus(User student, AppointmentStatus status);
    Optional<Appointment> findByStudentAndTutorAndStatus(User student, Tutor tutor, AppointmentStatus status) ;

    List<Appointment> findAllByStudentAndTutorAndStatusAndDate(
            User student, Tutor tutor, AppointmentStatus status, LocalDateTime date);

    //@Query("SELECT a FROM Appointment a WHERE a.tutor.id = :tutorId AND a.status = :status AND DATE(a.date) BETWEEN :startDate AND :endDate")
    @Query(value = "SELECT * FROM appointment a WHERE a.tutor_id = :tutorId AND a.status = :status AND DATE(a.date) BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Appointment> findByTutorAndStatusAndDateBetween(
            @Param("tutorId") Long tutorId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
