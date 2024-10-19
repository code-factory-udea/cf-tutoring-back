package co.udea.codefact.administration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProfessorSubjectDTO {

    @NotNull(message = "El id del profesor a eliminar no puede ser nulo")
    private Long idProfessor;
}
