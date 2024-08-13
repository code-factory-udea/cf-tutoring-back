package co.udea.codefact.tutor;

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
public class TutorDTO {
    
    private Long id;
    private String name;
    private String username;
    private String subjectInfo;
    private String academicProgramInfo;

}
