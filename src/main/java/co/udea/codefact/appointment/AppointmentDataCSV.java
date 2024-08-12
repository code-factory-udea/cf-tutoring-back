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
public class AppointmentDataCSV {
    
    private Long id;
    private String date;
    private Boolean isVirtual;
    private String appointmentStatus;
    private Long subjectId;
    private Long academicProgramId;
    private Integer calification; 

}