package co.udea.codefact.appointment.dto;

import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.utils.ValidEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentGetTutorDTO {

    @ValidEnum(enumClass = AppointmentStatus.class, message = "Invalid status value")
    private AppointmentStatus status;
}
