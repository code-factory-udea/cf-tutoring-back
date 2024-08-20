package co.udea.codefact.academic;

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
