package co.udea.codefact.administration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteSubjectDTO {

    @NotNull(message = "El id a eliminar no puede ser nulo")
    private Long id;
}
