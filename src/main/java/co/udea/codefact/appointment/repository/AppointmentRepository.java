package co.udea.codefact.appointment.repository;

import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByTutorAndStatus(Tutor tutor, AppointmentStatus status);
}
