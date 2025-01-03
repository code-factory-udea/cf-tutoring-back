package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.dto.TutorListDTO;
import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.user.service.UserService;
import co.udea.codefact.utils.auth.AuthenticationUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentFacade {

    private final AppointmentStudentService appointmentStudentService;
    private final AppointmentTutorService appointmentTutorService;
    private final AuthenticationUtil authenticationUtil;
    private final TutorService tutorService;
    private final UserService userService;

    public AppointmentFacade(TutorService tutorService,
                             UserService userService,
                             AppointmentStudentService appointmentStudentService,
                             AppointmentTutorService appointmentTutorService,
                             AuthenticationUtil authenticationUtil) {
        this.userService = userService;
        this.appointmentStudentService = appointmentStudentService;
        this.appointmentTutorService = appointmentTutorService;
        this.authenticationUtil = authenticationUtil;
        this.tutorService = tutorService;
    }

    public AppointmentDTO studentRequestAppointment(AppointmentCreationDTO appointmentCreationDTO){
        User student = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.requestAppointment(appointmentCreationDTO, student);
    }

    public List<AppointmentInfoDTO> studentAppointmentsRequest(AppointmentGetInfoDTO appointmentGetTutorDTO){
        User user = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.getAppointmentsRequestAsStudent(user, AppointmentStatus.valueOf(appointmentGetTutorDTO.getStatus()));
    }

    public List<TutorListDTO> studentGetTutorsBySubject(Long subjectId){
        return this.tutorService.getTutorsBySubject(subjectId);
    }

    public List<TutorScheduleDTO> studentGetTutorsSchedule(String username){
        return this.tutorService.getTutorScheduleSlots(username);
    }

    public String studentSatisfactionSurveyAppointment(SatisfactionSurveyDTO satisfactionSurveyDTO){
        User student = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.createSatisfactionSurvey(satisfactionSurveyDTO, student);
    }

    public String studentCancelAppointment(AppointmentIDDTO appointmentIDDTO){
        User student = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.cancelAppointment(appointmentIDDTO.getId(), student);
    }

    public String studentCancelAppointmentRequest(AppointmentIDDTO appointmentIDDTO){
        User student = this.userService.getUserByUsername(this.getUser());
        return this.appointmentStudentService.cancelAppointmentRequest(appointmentIDDTO.getId(), student);
    }

    public List<AppointmentInfoDTO> tutorAppointmentsRequest(AppointmentGetInfoDTO appointmentGetTutorDTO){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.getAppointmentsRequestAsTutor(tutor, AppointmentStatus.valueOf(appointmentGetTutorDTO.getStatus()));
    }

    public String tutorResponseToAppointment(AppointmentTutorResponseDTO tutorResponseDTO){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.responseToAppointment(tutor, tutorResponseDTO);
    }

    public String tutorCompleteAppointment(AppointmentIDDTO appointmentIDDTO){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.completeAppointment(tutor, appointmentIDDTO);
    }


    public AppointmentAllDataDTO getCompletedAppointmentInfo(Long appointmentId){
        Tutor tutor = this.tutorService.getTutorAuthenticated();
        return this.appointmentTutorService.getCompletedAppointmentInfo(tutor, appointmentId);
    }

    private String getUser() {
        return this.authenticationUtil.getAuthenticatedUser();
    }
}
