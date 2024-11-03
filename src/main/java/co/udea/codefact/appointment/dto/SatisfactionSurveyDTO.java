package co.udea.codefact.appointment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SatisfactionSurveyDTO {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Integer calification;

    @NotNull
    private String feedback;
}
