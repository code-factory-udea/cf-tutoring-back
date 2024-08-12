package co.udea.codefact.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private String tutorName;
    private String studentName;
    private Boolean isVirtual;
    private AppointmentStatus status;
    private String date;
    private String creationDate;

}
