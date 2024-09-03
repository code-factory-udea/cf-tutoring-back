package co.udea.codefact.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreationDTO {

    private Long tutorId;
    private Boolean isVirtual;
    // Cambiar la fecha 
    private String date;
    
}
