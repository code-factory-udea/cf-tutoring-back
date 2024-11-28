package co.udea.codefact.administration.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
public class GetAllAppointmentsDTO {

    private LocalDate initialDate;
    private LocalDate finalDate;
}
