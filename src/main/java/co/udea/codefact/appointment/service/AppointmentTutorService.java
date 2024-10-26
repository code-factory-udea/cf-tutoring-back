package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.AppointmentIDDTO;
import co.udea.codefact.appointment.dto.AppointmentTutorDTO;
import co.udea.codefact.appointment.dto.AppointmentTutorResponseDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.repository.AppointmentRepository;
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


    public AppointmentTutorService(AppointmentRepository appointmentRepository,
                                   NotificationEmailService notificationEmailService) {
        this.appointmentRepository = appointmentRepository;
        this.notificationEmailService = notificationEmailService;
    }

    public List<AppointmentTutorDTO> getAppointmentsRequestAsTutor(Tutor tutor, AppointmentStatus status) {
        List<AppointmentTutorDTO> appointmentTutorDTOS = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAllByTutorAndStatus(tutor, status)) {
            appointmentTutorDTOS.add(AppointmentMapper.toAppointmentTutorDTO(appointment));
        }
        return appointmentTutorDTOS;
    }

    public void cancelAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentRepository.save(appointment);
        this.notificationEmailService.sendAppointmentCancellationByTutorEmail(appointment);
    }

    public String responseToAppointment(Tutor tutor, AppointmentTutorResponseDTO tutorResponseDTO) {
        AppointmentResponse tutorResponse = AppointmentResponse.valueOf(tutorResponseDTO.getAppointmentResponse());
        return switch (tutorResponse) {
            case APPROVE -> approveAppointment(tutor, tutorResponseDTO.getId());
            case REJECT -> rejectAppointment(tutor, tutorResponseDTO.getId());
            default -> throw new InvalidBodyException(MessagesConstants.ERROR_RESPONSE_APPOINTMENT_INVALID);
        };
    }

    public String completeAppointment(Tutor tutor, AppointmentIDDTO appointmentIDDTO){
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentIDDTO.getId(), AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_COMPLETED;
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
        return this.appointmentRepository.findById(appointmentId).orElseThrow(); //Falta
    }

    private void validateTutorSchedule(Appointment appointment) {

    }
}
