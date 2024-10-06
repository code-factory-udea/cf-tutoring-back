package co.udea.codefact.academic.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyDTO {

    @NotNull(message = "El nombre de la facultad a crear no puede ser nulo")
    private String name;
    
}
