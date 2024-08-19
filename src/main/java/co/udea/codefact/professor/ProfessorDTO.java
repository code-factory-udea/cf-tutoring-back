package co.udea.codefact.professor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfessorDTO {

    private String name;
    private String username;
    private List<ProfessorSubjectInfo> professorSubjectInfo;
    
}

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class ProfessorSubjectInfo{

    private Long idProfessor;
    private String subjectInfo;
    private String academicProgramInfo;

}
