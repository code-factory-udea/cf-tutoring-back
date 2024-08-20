package co.udea.codefact.subject;

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

    private Long code;
    private Long newCode;
    private String name;
    private Long academicProgramId;
    
}
