package co.udea.codefact.subject.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequestDTO {

    @NotNull(message = ConstraintsConstants.SUBJECT_CODE_NOT_NULL)
    private Long code;
    @NotNull(message = ConstraintsConstants.SUBJECT_NAME_NOT_NULL)
    @NotBlank(message = ConstraintsConstants.SUBJECT_NAME_NOT_BLANK)
    private String name;
    @NotNull(message = ConstraintsConstants.ACADEMIC_PROGRAM_CODE_NOT_NULL)
    private Long academicProgramId;
    
}
