package co.udea.codefact.appointment.utils;

import co.udea.codefact.appointment.dto.AppointmentAllDataDTO;
import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.dto.AppointmentDataCSV;
import co.udea.codefact.appointment.dto.AppointmentInfoDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.appointment.entity.SatisfactionSurvey;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;

public class AppointmentMapper {

    public static AppointmentDTO toDTOWithInfoTutorStudent(Appointment appointment) {
        User student = appointment.getStudent();
        User tutor = appointment.getTutor().getUser();
        return AppointmentDTO.builder()
                .id(appointment.getId())
                .studentName(String.format(FormatConstants.FULLNAME_FORMAT,student.getFirstName(),student.getLastName()))
                .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutor.getFirstName(), tutor.getLastName()))
                .date(appointment.getDate().toString())
                .creationDate(appointment.getCreationDate().toString())
                .isVirtual(appointment.isVirtual())
                .status(appointment.getStatus())
                .build();
    }

    public static AppointmentInfoDTO toAppointmentInfoDTO(Appointment appointment, User user) {
        return AppointmentInfoDTO.builder()
                .id(appointment.getId())
                .name(String.format(FormatConstants.FULLNAME_FORMAT, user.getFirstName(), user.getLastName()))
                .date(appointment.getDate().toLocalDate().toString())
                .startTime(appointment.getDate().toLocalTime().toString().substring(0,5))
                .endTime(appointment.getDate().toLocalTime().plusHours(1).toString().substring(0,5))
                .isVirtual(appointment.isVirtual())
                .build();
    }

    public static AppointmentDataCSV toAppointmentDataCSV(Appointment appointment, Integer calification){
        return AppointmentDataCSV.builder()
                .id(appointment.getId())
                .date(appointment.getDate().toString())
                .isVirtual(appointment.isVirtual())
                .appointmentStatus(appointment.getStatus().toString())
                .subjectId(appointment.getTutor().getSubject().getId())
                .academicProgramId(appointment.getTutor().getSubject().getAcademicProgram().getId())
                .calification(calification)
                .build();
    }

    public static AppointmentAllDataDTO toAppointmentAllDataDTO(Appointment appointment, SatisfactionSurvey satisfactionSurvey){
        User student = appointment.getStudent();
        User tutor = appointment.getTutor().getUser();

        AppointmentAllDataDTO.AppointmentAllDataDTOBuilder appointmentDataBuilder = AppointmentAllDataDTO.builder()
                .studentName(String.format(FormatConstants.FULLNAME_FORMAT, student.getFirstName(), student.getLastName()))
                .tutorName(String.format(FormatConstants.FULLNAME_FORMAT, tutor.getFirstName(), tutor.getLastName()))
                .date(appointment.getDate().toLocalDate().toString())
                .hour(appointment.getDate().toLocalTime().toString().substring(0,5))
                .creationDate(appointment.getCreationDate().toString())
                .isVirtual(appointment.isVirtual())
                .status(appointment.getStatus());

        if (appointment.getStatus().equals(AppointmentStatus.COMPLETED) && satisfactionSurvey != null) {
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

}
