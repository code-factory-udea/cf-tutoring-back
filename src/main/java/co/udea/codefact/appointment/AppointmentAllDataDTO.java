package co.udea.codefact.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentAllDataDTO {

    private String studentName;
    private String tutorName;
    private String date;
    private String creationDate;
    private boolean isVirtual;
    private AppointmentStatus status;
    private String calification;
    private String feedback;
    private String calificationDate;
    
}
