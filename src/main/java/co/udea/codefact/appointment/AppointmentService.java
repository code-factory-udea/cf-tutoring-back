package co.udea.codefact.appointment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.tutor.Tutor;
import co.udea.codefact.tutor.TutorService;
import co.udea.codefact.user.User;
import co.udea.codefact.user.UserService;
import co.udea.codefact.utils.auth.AuthenticationUtil;

@Service
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final SatisfactionSurveyRepository satisfactionSurveyRepository;
    private final WaitingListRepository waitingListRepository;
    private final UserService userService;
    private final TutorService tutorService;
    private final AuthenticationUtil authenticationUtil;


    public AppointmentService(AppointmentRepository appointmentRepository, 
                                SatisfactionSurveyRepository satisfactionSurveyRepository, 
                                WaitingListRepository waitingListRepository, 
                                UserService userService,
                                TutorService tutorService,
                                AuthenticationUtil authenticationUtil) {
        this.appointmentRepository = appointmentRepository;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
        this.waitingListRepository = waitingListRepository;
        this.userService = userService;
        this.tutorService = tutorService;
        this.authenticationUtil = authenticationUtil;
    }

    public AppointmentDTO createAppointment(AppointmentCreationDTO appointmentCreationDTO) {
        User student = this.getUser();
        System.out.println(student.getUsername());
        Tutor tutor = this.tutorService.getTutorById(appointmentCreationDTO.getTutorId());
        // TODO: Obtener bien la fecha con el DTO
        LocalDateTime date = LocalDateTime.now();
        //
        Appointment appointment = Appointment.builder()
            .student(student)
            .tutor(tutor)
            .date(date)
            .isVirtual(appointmentCreationDTO.getIsVirtual())
            .status(AppointmentStatus.PENDING)
            .build();
        appointment = this.appointmentRepository.save(appointment);
        User tutorUser = tutor.getUser();
        return AppointmentDTO.builder()
            .id(appointment.getId())
            .studentName(String.format("%s %s",student.getFirstName(),student.getLastName()))
            .tutorName(String.format("%s %s", tutorUser.getFirstName(), tutorUser.getLastName()))
            .date(date.toString())
            .creationDate(appointment.getCreationDate().toString())
            .isVirtual(appointmentCreationDTO.getIsVirtual())
            .status(AppointmentStatus.PENDING)
            .build();
    }

    public List<AppointmentDataCSV> getAllAppointmentsToCSV() {
        List<AppointmentDataCSV> listAppointmentsCsvs = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAll()) {
            listAppointmentsCsvs.add(
                AppointmentDataCSV.builder()
                .id(appointment.getId())
                .date(appointment.getDate().toString())
                .isVirtual(appointment.getIsVirtual())
                .appointmentStatus(appointment.getStatus().toString())
                .subjectId(appointment.getTutor().getSubject().getId())
                .academicProgramId(appointment.getTutor().getSubject().getAcademicProgram().getId())
                .calification(this.getCalification(appointment.getId()))
                .build());
        }
        return listAppointmentsCsvs;
    }

    public List<AppointmentDTO> getAllAppointments() {
        List<AppointmentDTO> listAppointments = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAll()) {
            User student = appointment.getStudent();
            User tutor = appointment.getTutor().getUser();
            listAppointments.add(
                AppointmentDTO.builder()
                .id(appointment.getId())
                .studentName(String.format("%s %s",student.getFirstName(),student.getLastName()))
                .tutorName(String.format("%s %s", tutor.getFirstName(), tutor.getLastName()))
                .date(appointment.getDate().toString())
                .creationDate(appointment.getCreationDate().toString())
                .isVirtual(appointment.getIsVirtual())
                .status(appointment.getStatus())
                .build());
        }
        return listAppointments;
    }

    private Integer getCalification(Long appointmentId) {
        Optional<SatisfactionSurvey> satisfactionSurvey = this.satisfactionSurveyRepository.findByAppointmentId(appointmentId);
        if (satisfactionSurvey.isPresent()) {
            return satisfactionSurvey.get().getCalification();
        }
        return null;
    }

    private User getUser() {
        return this.userService.getUserByUsername(this.authenticationUtil.getAuthenticatedUser());
    }
}