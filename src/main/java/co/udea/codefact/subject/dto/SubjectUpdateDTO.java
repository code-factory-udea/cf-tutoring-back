package co.udea.codefact.subject.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
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
public class SubjectUpdateDTO {

    @NotNull(message = ConstraintsConstants.SUBJECT_CODE_NOT_NULL)
    private Long code;
    private Long newCode;
    private String name;
    private Long academicProgramId;
    
}
