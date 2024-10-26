package co.udea.codefact.appointment.dto;

import co.udea.codefact.appointment.utils.AppointmentResponse;
import co.udea.codefact.utils.validators.ValidEnum;
import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentTutorResponseDTO {

    @NotNull(message = ConstraintsConstants.ID_NOT_NULL)
    private Long id;
    @NotNull(message = ConstraintsConstants.APPOINTMENT_RESPONSE_NOT_NULL)
    @ValidEnum(enumClass = AppointmentResponse.class)
    private String appointmentResponse;

}
