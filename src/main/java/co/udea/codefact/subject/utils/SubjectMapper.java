package co.udea.codefact.subject.utils;

import co.udea.codefact.subject.dto.SubjectListDTO;
import co.udea.codefact.subject.dto.SubjectRequestDTO;
import co.udea.codefact.subject.dto.SubjectResponseDTO;
import co.udea.codefact.subject.entity.Subject;

public class SubjectMapper {
        
        public static SubjectResponseDTO toDTO(Subject subject) {
            return SubjectResponseDTO.builder()
                    .code(subject.getCode())
                    .name(subject.getName())
                    .academicProgram(subject.getAcademicProgram().getName())
                    .build();
        }

        public static Subject toEntity(SubjectRequestDTO subjectDTO) {
            return Subject.builder()
                    .code(subjectDTO.getCode())
                    .name(subjectDTO.getName())
                    .build();
        }

        public static SubjectListDTO toListDTO(Subject subject) {
            return SubjectListDTO.builder()
                    .code(subject.getCode())
                    .name(subject.getName())
                    .build();
        }
    
}
