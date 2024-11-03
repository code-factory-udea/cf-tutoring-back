package co.udea.codefact.appointment.dto;

import co.udea.codefact.tutor.dto.TutorScheduleDTO;
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

    private String tutorUsername;
    private Boolean isVirtual;
    private TutorScheduleDTO schedule;
    
}
