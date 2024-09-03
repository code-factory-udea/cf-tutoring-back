package co.udea.codefact.professor.dto;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfessorSubjectInfoDTO {

    private Long idProfessor;
    private String subjectInfo;
    private String academicProgramInfo;

}
