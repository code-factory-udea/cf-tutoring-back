package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.AppointmentCreationDTO;
import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.repository.AppointmentRepository;
import co.udea.codefact.appointment.utils.AppointmentMapper;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentStudentService {

    private final TutorService tutorService;
    private final AppointmentRepository appointmentRepository;

    public AppointmentStudentService(TutorService tutorService, AppointmentRepository appointmentRepository) {
        this.tutorService = tutorService;
        this.appointmentRepository  = appointmentRepository;
    }

    public AppointmentDTO requestAppointment(AppointmentCreationDTO appointmentCreationDTO, User student) {
        String role = student.getRole().getRole();
        if (!role.equals(RoleConstants.TUTOR) && !role.equals(RoleConstants.STUDENT)) {
            throw new DataNotFoundException(MessagesConstants.NO_PERMISSION);
        }
        Tutor tutor = this.tutorService.getTutorById(appointmentCreationDTO.getTutorId());
        // TODO: Obtener bien la fecha con el DTO
        LocalDateTime date = LocalDateTime.now();
        //
        Appointment appointment = Appointment.builder()
                .student(student)
                .tutor(tutor)
                .date(date)
                .subject(tutor.getSubject())
                .isVirtual(appointmentCreationDTO.getIsVirtual())
                .status(AppointmentStatus.PENDING)
                .build();
        appointment = this.appointmentRepository.save(appointment);
        return AppointmentMapper.toDTOWithInfoTutorStudent(appointment);
    }

}
