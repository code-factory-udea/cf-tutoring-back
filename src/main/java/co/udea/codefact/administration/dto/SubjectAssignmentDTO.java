package co.udea.codefact.administration.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectAssignmentDTO {

    @NotNull(message = ConstraintsConstants.USERNAME_NOT_NULL)
    @Size(min = 1, max = 50, message = ConstraintsConstants.USERNAME_SIZE)
    private String username;
    @NotNull(message = ConstraintsConstants.SUBJECT_CODE_NOT_NULL)
    private Long subjectCode;

}