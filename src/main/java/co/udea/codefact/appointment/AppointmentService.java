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
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

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
            .isVirtual(appointmentCreationDTO.getIsVirtual())
            .status(AppointmentStatus.PENDING)
            .build();
        appointment = this.appointmentRepository.save(appointment);
        User tutorUser = tutor.getUser();
        return AppointmentDTO.builder()
            .id(appointment.getId())
            .studentName(String.format(FormatConstants.FULLNAME_FORMAT,student.getFirstName(),student.getLastName()))
            .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutorUser.getFirstName(), tutorUser.getLastName()))
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
                .studentName(String.format(FormatConstants.FULLNAME_FORMAT,student.getFirstName(),student.getLastName()))
                .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutor.getFirstName(), tutor.getLastName()))
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

    public AppointmentAllDataDTO getAppointmentByIdAsAdmin(Long appointmentId) {
        Appointment appointment = this.getAppointment(appointmentId);
        return this.getAppointmentData(appointment);
    }

    private AppointmentAllDataDTO getAppointmentData(Appointment appointment) {
        User student = appointment.getStudent();
        User tutor = appointment.getTutor().getUser();

        AppointmentAllDataDTO.AppointmentAllDataDTOBuilder appointmentDataBuilder = AppointmentAllDataDTO.builder()
            .studentName(String.format(FormatConstants.FULLNAME_FORMAT,student.getFirstName(),student.getLastName()))
            .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutor.getFirstName(), tutor.getLastName()))
            .date(appointment.getDate().toString())
            .creationDate(appointment.getCreationDate().toString())
            .isVirtual(appointment.getIsVirtual())
            .status(appointment.getStatus());

        if (appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
            SatisfactionSurvey satisfactionSurvey = this.getSatisfactionSurvey(appointment.getId());
            appointmentDataBuilder.feedback(satisfactionSurvey.getFeedback());
            appointmentDataBuilder.calification(satisfactionSurvey.getCalification().toString());    
            appointmentDataBuilder.calificationDate(satisfactionSurvey.getCreationDate().toString());
            return appointmentDataBuilder.build();
        }
        
        appointmentDataBuilder.calification(MessagesConstants.NO_DATA);
        appointmentDataBuilder.feedback(MessagesConstants.NO_DATA);
        appointmentDataBuilder.calificationDate(MessagesConstants.NO_DATA);
        return appointmentDataBuilder.build();
    }

    private Appointment getAppointment(Long appointmentId) {
        return this.appointmentRepository.findById(appointmentId).orElseThrow(() -> new DataNotFoundException(MessagesConstants.APPOINTMENT_NOT_FOUND));
    }

    private SatisfactionSurvey getSatisfactionSurvey(Long appointmentId) {
        return this.satisfactionSurveyRepository.findByAppointmentId(appointmentId).orElseThrow(() -> new DataNotFoundException(MessagesConstants.APPOINTMENT_NOT_FOUND));
    }

    private User getUser() {
        return this.userService.getUserByUsername(this.authenticationUtil.getAuthenticatedUser());
    }
}