package co.udea.codefact.appointment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTutorDTO {

    private Long id;
    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private boolean isVirtual;

}
