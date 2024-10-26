package co.udea.codefact.tutor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class TutorScheduleDTO {

    @NotNull
    private DayOfWeek day;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

}
