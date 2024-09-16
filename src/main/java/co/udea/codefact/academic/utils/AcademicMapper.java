package co.udea.codefact.academic.utils;

import co.udea.codefact.academic.dto.AcademicProgramResponseDTO;
import co.udea.codefact.academic.dto.FacultyResponseDTO;
import co.udea.codefact.academic.entity.AcademicProgram;
import co.udea.codefact.academic.entity.Faculty;

public class AcademicMapper {
    
    public static AcademicProgramResponseDTO toAcademicProgramResponseDTO(AcademicProgram academicProgram) {
        return AcademicProgramResponseDTO.builder()
                .id(academicProgram.getId())
                .name(academicProgram.getName())
                .build();
    }

    public static FacultyResponseDTO toFacultyResponseDTO(Faculty faculty) {
        return FacultyResponseDTO.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }
}
