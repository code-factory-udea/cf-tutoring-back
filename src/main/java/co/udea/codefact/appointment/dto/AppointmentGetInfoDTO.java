package co.udea.codefact.appointment.dto;

import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.utils.validators.ValidEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentGetInfoDTO {

    @ValidEnum(enumClass = AppointmentStatus.class, message = "Invalid status value")
    private String status;
}
