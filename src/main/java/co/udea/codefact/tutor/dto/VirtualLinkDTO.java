package co.udea.codefact.tutor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VirtualLinkDTO {

    @NotNull(message = "El link no puede estar vacío")
    private String link;
}
