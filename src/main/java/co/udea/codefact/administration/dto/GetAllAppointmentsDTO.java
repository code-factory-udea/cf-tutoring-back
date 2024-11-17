package co.udea.codefact.administration.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetAllAppointmentsDTO {

    LocalDate initialDate;
    LocalDate finalDate;
}
