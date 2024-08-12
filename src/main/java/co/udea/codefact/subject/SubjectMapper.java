package co.udea.codefact.subject;

public class SubjectMapper {
        
        public static SubjectResponseDTO toDTO(Subject subject) {
            return SubjectResponseDTO.builder()
                    .id(subject.getId())
                    .name(subject.getName())
                    .academicProgram(subject.getAcademicProgram().getName())
                    .build();
        }
        
        public static Subject toEntity(SubjectRequestDTO subjectDTO) {
            return Subject.builder()
                    .id(subjectDTO.getId())
                    .name(subjectDTO.getName())
                    .build();
        }
    
}
