package co.udea.codefact.appointment.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class AppointmentIDDTO {

    @NotNull(message = ConstraintsConstants.ID_NOT_NULL)
    private Long id;
}
