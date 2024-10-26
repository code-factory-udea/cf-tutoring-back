package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.user.service.UserService;
import co.udea.codefact.utils.auth.AuthenticationUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentFacade {

    private final AppointmentService appointmentService;
    private final AppointmentStudentService appointmentStudentService;
    private final AppointmentTutorService appointmentTutorService;
    private final AuthenticationUtil authenticationUtil;
    private final TutorService tutorService;
    private final UserService userService;

    public AppointmentFacade(AppointmentService appointmentService,
                             TutorService tutorService,
                             UserService userService,
                             AppointmentStudentService appointmentStudentService,
                             AppointmentTutorService appointmentTutorService,
                             AuthenticationUtil authenticationUtil) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.appointmentStudentService = appointmentStudentService;
        this.appointmentTutorService = appointmentTutorService;
        this.authenticationUtil = authenticationUtil;
        this.tutorService = tutorService;
    }

    public AppointmentDTO studentRequestAppointment(AppointmentCreationDTO appointmentCreationDTO){
        User user = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.requestAppointment(appointmentCreationDTO, user);
    }

    public List<AppointmentTutorDTO> tutorAppointmentsRequest(AppointmentGetTutorDTO appointmentGetTutorDTO){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.getAppointmentsRequestAsTutor(tutor, AppointmentStatus.valueOf(appointmentGetTutorDTO.getStatus()));
    }

    public String tutorResponseToAppointment(AppointmentTutorResponseDTO tutorResponseDTO){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.responseToAppointment(tutor, tutorResponseDTO);

    }
    private String getUser() {
        return this.authenticationUtil.getAuthenticatedUser();
    }
}
