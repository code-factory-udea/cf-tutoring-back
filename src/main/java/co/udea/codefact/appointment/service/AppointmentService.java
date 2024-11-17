package co.udea.codefact.appointment.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.repository.AppointmentRepository;
import co.udea.codefact.appointment.repository.SatisfactionSurveyRepository;
import co.udea.codefact.appointment.utils.AppointmentMapper;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import org.springframework.stereotype.Service;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SatisfactionSurveyRepository satisfactionSurveyRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              SatisfactionSurveyRepository satisfactionSurveyRepository) {
        this.appointmentRepository = appointmentRepository;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
    }

    public List<AppointmentDataCSV> getAllAppointmentsToCSV(LocalDate initialDate, LocalDate finalDate) {
        List<AppointmentDataCSV> listAppointmentsCsvs = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAllBetweenDates(initialDate, finalDate)) {
            listAppointmentsCsvs.add(
                    AppointmentMapper.toAppointmentDataCSV(appointment, this.getCalification(appointment.getId()))
            );
        }
        return listAppointmentsCsvs;
    }

    public List<AppointmentDTO> getAllAppointmentsBetweenDates(LocalDate initialDate, LocalDate finalDate) {
        List<AppointmentDTO> listAppointments = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAllBetweenDates(initialDate, finalDate)) {
            listAppointments.add(AppointmentMapper.toDTOWithInfoTutorStudent(appointment));
        }
        return listAppointments;
    }

    public List<AppointmentDTO> getAllAppointmentsByTutorAndDate(Tutor tutor,
                                                                 LocalDate initialDate, LocalDate finalDate){
        List<AppointmentDTO> listAppointmentsDTO = new ArrayList<>();
        List<Appointment> listAppointments = this.appointmentRepository.findByTutorAndStatusAndDateBetween(tutor.getId(),
                AppointmentStatus.COMPLETED.toString(), initialDate, finalDate);
        for (Appointment appointment : listAppointments) {
            listAppointmentsDTO.add(AppointmentMapper.toDTOWithInfoTutorStudent(appointment));
        }
        return listAppointmentsDTO;
    }

    private Integer getCalification(Long appointmentId) {
        Optional<SatisfactionSurvey> satisfactionSurvey = this.satisfactionSurveyRepository.findByAppointmentId(appointmentId);
        return satisfactionSurvey.map(SatisfactionSurvey::getCalification).orElse(null);
    }

    public AppointmentAllDataDTO getAppointmentByIdAsAdmin(Long appointmentId) {
        Appointment appointment = this.getAppointment(appointmentId);
        return this.getAppointmentData(appointment);
    }

    private AppointmentAllDataDTO getAppointmentData(Appointment appointment) {
        SatisfactionSurvey satisfactionSurvey = this.satisfactionSurveyRepository
                .findByAppointmentId(appointment.getId()).orElse(null);
        return AppointmentMapper.toAppointmentAllDataDTO(appointment, satisfactionSurvey);
    }

    private Appointment getAppointment(Long appointmentId) {
        return this.appointmentRepository.findById(appointmentId).orElseThrow(() -> new DataNotFoundException(MessagesConstants.APPOINTMENT_NOT_FOUND));
    }

}