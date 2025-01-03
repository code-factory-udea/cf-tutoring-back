package co.udea.codefact.academic.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAcademicProgramDTO {

    @NotNull(message = ConstraintsConstants.ACADEMIC_PROGRAM_CODE_NOT_NULL)
    private Long id;
    private String name;
    private Long facultyId;

}





