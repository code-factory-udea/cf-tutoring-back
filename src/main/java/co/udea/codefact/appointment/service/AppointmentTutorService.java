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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Service
public class AppointmentTutorService {
    private final AppointmentRepository appointmentRepository;
    private final SatisfactionSurveyRepository satisfactionSurveyRepository;
    private final NotificationEmailService notificationEmailService;


    public AppointmentTutorService(AppointmentRepository appointmentRepository,
                                   NotificationEmailService notificationEmailService,
                                   SatisfactionSurveyRepository satisfactionSurveyRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationEmailService = notificationEmailService;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
    }

    public List<AppointmentInfoDTO> getAppointmentsRequestAsTutor(Tutor tutor, AppointmentStatus status) {
        List<AppointmentInfoDTO> appointmentTutorDTOS = new ArrayList<>();
        if (!tutor.isActive()) {
            updateStatusInactiveTutor(tutor);
        }
        for (Appointment appointment : this.appointmentRepository.findAllByTutorAndStatus(tutor, status)) {
            appointmentTutorDTOS.add(AppointmentMapper.toAppointmentInfoDTO(appointment, appointment.getStudent()));
        }
        return appointmentTutorDTOS;
    }

    private void updateStatusInactiveTutor(Tutor tutor) {
        List<Appointment> appointments = Stream.concat(
                this.appointmentRepository.findAllByTutorAndStatus(tutor, AppointmentStatus.PENDING).stream(),
                this.appointmentRepository.findAllByTutorAndStatus(tutor, AppointmentStatus.ACCEPTED).stream())
                .toList();
        for (Appointment appointment : appointments) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            this.appointmentRepository.save(appointment);
        }
    }
    public String completeAppointment(Tutor tutor, AppointmentIDDTO appointmentIDDTO){
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentIDDTO.getId(), AppointmentStatus.ACCEPTED);
        if (LocalDateTime.now().isBefore(appointment.getDate())) {
            throw new InvalidBodyException(MessagesConstants.DATE_BEFORE);
        }
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
            case CANCEL -> cancelAppointment(tutor, tutorResponseDTO.getId());
        };
    }

    private String approveAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.PENDING);
        this.changeAppointmentDate(appointment, LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.ACCEPTED);
        this.appointmentRepository.save(appointment);
        this.changeAllAppointmentsRequestOnSameTime(appointment);
        notificationEmailService.sendAppointmentConfirmationEmail(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_ACCEPTED;
    }

    private void changeAppointmentDate(Appointment appointment, LocalDateTime now){
        LocalDateTime date = appointment.getDate();
        if (date.isBefore(now) || date.isEqual(now)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            LocalTime time = date.toLocalTime();
            LocalDate nextDate = now.toLocalDate().with(TemporalAdjusters.next(dayOfWeek));
            appointment.setDate(LocalDateTime.of(nextDate, time));
        }
    }

    @Async
    public void changeAllAppointmentsRequestOnSameTime(Appointment appointment){
        List<Appointment> appointments = this.appointmentRepository.findAllByStudentAndTutorAndStatusAndDate(
                appointment.getStudent(),
                appointment.getTutor(),
                AppointmentStatus.PENDING,
                appointment.getDate());
        for (Appointment appointmentModify : appointments) {
            changeAppointmentDate(appointmentModify, appointmentModify.getDate());
        }
    }

    private String rejectAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.PENDING);
        appointment.setStatus(AppointmentStatus.REJECTED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_REJECTED;
    }

    public String cancelAppointment(Tutor tutor, Long appointmentId) {
        Appointment appointment = this.getAndValidateAppointment(tutor, appointmentId, AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentRepository.save(appointment);
        notificationEmailService.sendAppointmentCancellationByTutorEmail(appointment);
        return MessagesConstants.RESPONSE_TUTOR_APPOINTMENT_CANCELLED;
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

}
