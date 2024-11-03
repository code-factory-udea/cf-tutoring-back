package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.AppointmentCreationDTO;
import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.dto.AppointmentInfoDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.repository.AppointmentRepository;
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

    public AppointmentStudentService(TutorService tutorService, AppointmentRepository appointmentRepository) {
        this.tutorService = tutorService;
        this.appointmentRepository = appointmentRepository;
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
