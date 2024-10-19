package co.udea.codefact.tutor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class TutorScheduleDTO {

    @NotNull
    private String day;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

}
