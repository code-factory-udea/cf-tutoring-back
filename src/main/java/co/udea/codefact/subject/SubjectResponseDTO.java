package co.udea.codefact.subject;

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

    private Long id;
    private String name;
    private String academicProgram;
    
}