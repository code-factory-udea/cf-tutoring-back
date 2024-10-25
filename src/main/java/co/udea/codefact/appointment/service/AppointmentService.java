package co.udea.codefact.appointment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.repository.AppointmentRepository;
import co.udea.codefact.appointment.repository.SatisfactionSurveyRepository;
import co.udea.codefact.appointment.repository.WaitingListRepository;
import co.udea.codefact.appointment.utils.AppointmentMapper;
import co.udea.codefact.appointment.utils.AppointmentStatus;
import org.springframework.stereotype.Service;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.user.service.UserService;
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

    public AppointmentService(AppointmentRepository appointmentRepository,
                              SatisfactionSurveyRepository satisfactionSurveyRepository,
                              WaitingListRepository waitingListRepository) {
        this.appointmentRepository = appointmentRepository;
        this.satisfactionSurveyRepository = satisfactionSurveyRepository;
        this.waitingListRepository = waitingListRepository;
    }

    public List<AppointmentDataCSV> getAllAppointmentsToCSV() {
        List<AppointmentDataCSV> listAppointmentsCsvs = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAll()) {
            listAppointmentsCsvs.add(
                    AppointmentMapper.toAppointmentDataCSV(appointment, this.getCalification(appointment.getId()))
            );
        }
        return listAppointmentsCsvs;
    }

    public List<AppointmentDTO> getAllAppointments() {
        List<AppointmentDTO> listAppointments = new ArrayList<>();
        for (Appointment appointment : this.appointmentRepository.findAll()) {
            listAppointments.add(AppointmentMapper.toDTOWithInfoTutorStudent(appointment));
        }
        return listAppointments;
    }

    private Integer getCalification(Long appointmentId) {
        Optional<SatisfactionSurvey> satisfactionSurvey = this.satisfactionSurveyRepository.findByAppointmentId(appointmentId);
        return satisfactionSurvey.map(SatisfactionSurvey::getCalification).orElse(null);
    }

    public AppointmentAllDataDTO getAppointmentByIdAsAdmin(Long appointmentId) {
        Appointment appointment = this.getAppointment(appointmentId);
        return this.getAppointmentData(appointment);
    }

    private AppointmentAllDataDTO getAppointmentData(Appointment appointment) {
        User student = appointment.getStudent();
        User tutor = appointment.getTutor().getUser();

        AppointmentAllDataDTO.AppointmentAllDataDTOBuilder appointmentDataBuilder = AppointmentAllDataDTO.builder()
                .studentName(String.format(FormatConstants.FULLNAME_FORMAT, student.getFirstName(), student.getLastName()))
                .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutor.getFirstName(), tutor.getLastName()))
                .date(appointment.getDate().toString())
                .creationDate(appointment.getCreationDate().toString())
                .isVirtual(appointment.isVirtual())
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


}