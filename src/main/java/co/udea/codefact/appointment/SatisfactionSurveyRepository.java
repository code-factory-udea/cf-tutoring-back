package co.udea.codefact.appointment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionSurveyRepository extends JpaRepository<SatisfactionSurvey, Long> {

    Optional<SatisfactionSurvey> findByAppointmentId(Long appointmentId);
    
}
