package co.udea.codefact.tutor.dto;

import co.udea.codefact.utils.validators.ValidEnum;
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
    @ValidEnum(enumClass = DayOfWeek.class, message = "Invalid status value")
    private String day;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

}
