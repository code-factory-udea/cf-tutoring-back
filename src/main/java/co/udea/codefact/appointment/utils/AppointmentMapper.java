package co.udea.codefact.appointment.utils;

import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.dto.AppointmentDataCSV;
import co.udea.codefact.appointment.dto.AppointmentTutorDTO;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;

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

    public static AppointmentTutorDTO toAppointmentTutorDTO(Appointment appointment) {
        User student = appointment.getStudent();
        return AppointmentTutorDTO.builder()
                .id(appointment.getId())
                .name(String.format(FormatConstants.FULLNAME_FORMAT, student.getFirstName(), student.getLastName()))
                .date(appointment.getDate().toString())
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

}
