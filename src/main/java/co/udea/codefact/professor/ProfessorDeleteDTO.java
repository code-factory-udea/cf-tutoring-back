package co.udea.codefact.professor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfessorDeleteDTO {
    
    private Long id;
    private Long subjectCode;
}
