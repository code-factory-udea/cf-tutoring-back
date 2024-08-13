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
    
    public Long id;
    public String name;
    public String username;
    public String subjectInfo;
    public String academicProgramInfo;

}
