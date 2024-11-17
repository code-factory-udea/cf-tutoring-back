package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.AppointmentCreationDTO;
import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.dto.AppointmentInfoDTO;
import co.udea.codefact.appointment.dto.SatisfactionSurveyDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import co.udea.codefact.appointment.repository.AppointmentRepository;
import co.udea.codefact.appointment.repository.SatisfactionSurveyRepository;
import co.udea.codefact.appointment.utils.AppointmentMapper;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataAlreadyExistsException;
import co.udea.codefact.utils.exceptions.DataNotFoundException;
import co.udea.codefact.utils.exceptions.InvalidBodyException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentStudentService {

    private final TutorService tutorService;
    private final AppointmentRepository appointmentRepository;
    private final SatisfactionSurveyRepository satisfactionSurveyRepository;
    private final NotificationEmailService notificationEmailService;

    public AppointmentStudentService(TutorService tutorService,
                                     AppointmentRepository appointmentRepository,
                                     SatisfactionSurveyRepository satisfactionSurveyRepository,
                                     NotificationEmailService notificationEmailService) {
        this.tutorService = tutorService;
        this.appointmentRepository = appointmentRepository;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
        this.notificationEmailService = notificationEmailService;
    }

    public AppointmentDTO requestAppointment(AppointmentCreationDTO appointmentCreation, User student) {
        String role = student.getRole().getRole();
        if (!role.equals(RoleConstants.TUTOR) && !role.equals(RoleConstants.STUDENT)) {
            throw new DataNotFoundException(MessagesConstants.NO_PERMISSION);
        }
        Tutor tutor = this.tutorService.getTutorByUsername(appointmentCreation.getTutorUsername())
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
        Optional<Appointment> pendingAppointment = this.appointmentRepository
                .findByStudentAndTutorAndStatus(student, tutor, AppointmentStatus.PENDING);
        if (pendingAppointment.isPresent()) {
            throw new DataAlreadyExistsException(MessagesConstants.APPOINTMENT_ALREADY_EXISTS);
        }
        TutorSchedule tutorSchedule = this.tutorService.getTutorSchedule(appointmentCreation.getSchedule().getId());
        this.scheduleValidation(tutorSchedule, appointmentCreation.getSchedule());
        LocalDateTime date = this.getNextDate(appointmentCreation.getSchedule());
        Appointment appointment = Appointment.builder()
                .student(student)
                .tutor(tutor)
                .date(date)
                .subject(tutor.getSubject())
                .isVirtual(appointmentCreation.getIsVirtual())
                .status(AppointmentStatus.PENDING)
                .build();
        appointment = this.appointmentRepository.save(appointment);
        return AppointmentMapper.toDTOWithInfoTutorStudent(appointment);
    }

    public List<AppointmentInfoDTO> getAppointmentsRequestAsStudent(User student, AppointmentStatus status) {
        List<AppointmentInfoDTO> appointmentInfoDTOS = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAllByStudentAndStatus(student, status)) {
            appointmentInfoDTOS.add(AppointmentMapper.toAppointmentInfoDTO(appointment, appointment.getTutor().getUser()));
        }
        return appointmentInfoDTOS;
    }

    public String cancelAppointment(Long appointmentId, User student) {
        Appointment appointment = this.getAndValidateAppointment(student, appointmentId, AppointmentStatus.ACCEPTED);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentRepository.save(appointment);
        this.notificationEmailService.sendAppointmentCancellationByStudentEmail(appointment);
        return MessagesConstants.RESPONSE_STUDENT_APPOINTMENT_CANCELLED;
    }

    public String cancelAppointmentRequest(Long appointmentId, User student) {
        Appointment appointment = this.getAndValidateAppointment(student, appointmentId, AppointmentStatus.PENDING);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_STUDENT_APPOINTMENT_REQUEST_CANCELLED;
    }

    public String createSatisfactionSurvey(SatisfactionSurveyDTO satisfactionDTO, User student){
        Appointment appointment = this.getAndValidateAppointment(
                student, satisfactionDTO.getAppointmentId(), AppointmentStatus.COMPLETED);
        Optional<SatisfactionSurvey> satisfactionSurveyExist = this.satisfactionSurveyRepository
                .findByAppointmentId(appointment.getId());
        if (satisfactionSurveyExist.isPresent()) {
            throw new DataAlreadyExistsException(MessagesConstants.APPOINTMENT_ALREADY_QUALIFIED);
        }
        this.satisfactionSurveyRepository.save(SatisfactionSurvey.builder()
                .appointment(appointment)
                .calification(satisfactionDTO.getCalification())
                .feedback(satisfactionDTO.getFeedback())
                .build());
        appointment.setStatus(AppointmentStatus.FINISHED);
        this.appointmentRepository.save(appointment);
        return MessagesConstants.RESPONSE_APPOINTMENT_QUALIFIED;
    }

    private Appointment getAndValidateAppointment(User student, Long appointmentId, AppointmentStatus status) {
        Appointment appointment = getAppointment(appointmentId);
        if (!appointment.getStatus().equals(status)) {
            throw new InvalidBodyException(MessagesConstants.TUTOR_APPOINTMENT_FINAL_STATUS);
        }
        if (!appointment.getStudent().equals(student)) {
            throw new DataNotFoundException(MessagesConstants.NO_PERMISSION);
        }
        return appointment;
    }

    private Appointment getAppointment(Long appointmentId) {
        return this.appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new DataNotFoundException(MessagesConstants.APPOINTMENT_NOT_FOUND));
    }

    private LocalDateTime getNextDate(TutorScheduleDTO schedule) {
        LocalDate today = LocalDate.now();
        LocalTime startTime = schedule.getStartTime();
        DayOfWeek targetDay = DayOfWeek.valueOf(schedule.getDay());
        if (today.getDayOfWeek() == targetDay && LocalTime.now().isBefore(startTime)) {
            return LocalDateTime.of(today, startTime);
        }
        LocalDate nextDate = today.with(TemporalAdjusters.next(targetDay));
        return LocalDateTime.of(nextDate, startTime);
    }

    private void scheduleValidation(TutorSchedule schedule, TutorScheduleDTO dto) {
        LocalTime dtoStart = dto.getStartTime();
        LocalTime dtoEnd = dto.getEndTime();
        LocalTime scheduleStart = schedule.getStartTime();
        LocalTime scheduleEnd = schedule.getEndTime();
        if (!((dtoStart.equals(scheduleStart) || dtoStart.isAfter(scheduleStart))
                && (dtoEnd.equals(scheduleEnd) || dtoEnd.isBefore(scheduleEnd)))) {
            throw new DataNotFoundException(MessagesConstants.ERROR_TUTOR_SCHEDULE_NO_EXISTS);
        }
    }
}
