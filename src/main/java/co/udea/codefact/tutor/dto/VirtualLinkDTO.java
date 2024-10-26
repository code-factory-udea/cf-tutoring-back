package co.udea.codefact.tutor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualLinkDTO {

    @NotNull(message = "El link no puede estar vacío")
    private String link;
}
