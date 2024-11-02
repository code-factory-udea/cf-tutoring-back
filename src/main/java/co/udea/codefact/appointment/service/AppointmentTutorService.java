package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.AppointmentAllDataDTO;
import co.udea.codefact.appointment.dto.AppointmentIDDTO;
import co.udea.codefact.appointment.dto.AppointmentInfoDTO;
import co.udea.codefact.appointment.dto.AppointmentTutorResponseDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import co.udea.codefact.appointment.repository.AppointmentRepository;
import co.udea.codefact.appointment.repository.SatisfactionSurveyRepository;
import co.udea.codefact.appointment.utils.AppointmentResponse;
import co.udea.codefact.appointment.utils.AppointmentMapper;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;
import co.udea.codefact.utils.exceptions.InvalidBodyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AppointmentTutorService {
    private final AppointmentRepository appointmentRepository;
    private final NotificationEmailService notificationEmailService;
    private final SatisfactionSurveyRepository satisfactionSurveyRepository;


    public AppointmentTutorService(AppointmentRepository appointmentRepository,
                                   NotificationEmailService notificationEmailService,
                                   SatisfactionSurveyRepository satisfactionSurveyRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationEmailService = notificationEmailService;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
    }

    public List<AppointmentInfoDTO> getAppointmentsRequestAsTutor(Tutor tutor, AppointmentStatus status) {
        List<AppointmentInfoDTO> appointmentTutorDTOS = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAllByTutorAndStatus(tutor, status)) {
            appointmentTutorDTOS.add(AppointmentMapper.toAppointmentInfoDTO(appointment, appointment.getStudent()));
        }
        return appointmentTutorDTOS;
    }

    public String cancelAppointment(Tutor tutor, AppointmentIDDTO appointmentIDDTO) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentIDDTO.getId(), AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentRepository.save(appointment);
        this.notificationEmailService.sendAppointmentCancellationByTutorEmail(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_CANCELLED;
    }

    public String completeAppointment(Tutor tutor, AppointmentIDDTO appointmentIDDTO){
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentIDDTO.getId(), AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_COMPLETED;
    }

    public AppointmentAllDataDTO getCompletedAppointmentInfo(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.COMPLETED);
        SatisfactionSurvey satisfactionSurvey = this.satisfactionSurveyRepository
                .findByAppointmentId(appointment.getId()).orElse(null);
        return AppointmentMapper.toAppointmentAllDataDTO(appointment, satisfactionSurvey);
    }

    public String responseToAppointment(Tutor tutor, AppointmentTutorResponseDTO tutorResponseDTO) {
        AppointmentResponse tutorResponse = AppointmentResponse.valueOf(tutorResponseDTO.getAppointmentResponse());
        return switch (tutorResponse) {
            case APPROVE -> approveAppointment(tutor, tutorResponseDTO.getId());
            case REJECT -> rejectAppointment(tutor, tutorResponseDTO.getId());
            default -> throw new InvalidBodyException(MessagesConstants.ERROR_RESPONSE_APPOINTMENT_INVALID);
        };
    }

    private String approveAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.PENDING);
        this.validateTutorSchedule(appointment);
        appointment.setStatus(AppointmentStatus.ACCEPTED);
        //TODO: Falta rechazar las solicitudes que tienen la misma fecha y hora
        this.appointmentRepository.save(appointment);
        this.notificationEmailService.sendAppointmentConfirmationEmail(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_ACCEPTED;
    }

    private String rejectAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.PENDING);
        appointment.setStatus(AppointmentStatus.REJECTED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_REJECTED;
    }

    private Appointment getAndValidateAppointment(Tutor tutor, Long appointmentId, AppointmentStatus status) {
        Appointment appointment = getAppointment(appointmentId);
        if (!appointment.getStatus().equals(status)) {
            throw new InvalidBodyException(MessagesConstants.TUTOR_APPOINTMENT_FINAL_STATUS);
        }
        if (!appointment.getTutor().equals(tutor)) {
            throw new DataNotFoundException(MessagesConstants.NO_PERMISSION);
        }
        return appointment;
    }

    private Appointment getAppointment(Long appointmentId) {
        return this.appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new DataNotFoundException(MessagesConstants.APPOINTMENT_NOT_FOUND));
    }

    //TODO: Validar que tenga disponibilidad
    private void validateTutorSchedule(Appointment appointment) {

    }
}
