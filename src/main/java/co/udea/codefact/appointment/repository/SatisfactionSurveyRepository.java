package co.udea.codefact.appointment.repository;

import java.util.Optional;

import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionSurveyRepository extends JpaRepository<SatisfactionSurvey, Long> {

    Optional<SatisfactionSurvey> findByAppointmentId(Long appointmentId);
    
}
