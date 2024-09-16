package co.udea.codefact.subject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponseDTO {

    private Long code;
    private String name;
    private String academicProgram;
    
}
