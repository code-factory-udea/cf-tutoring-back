package co.udea.codefact.academic;

import org.springframework.stereotype.Service;

import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataAlreadyExistsException;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class AcademicService {
    
    private final FacultyRepository facultyRepository;
    private final AcademicProgramRepository academicProgramRepository;

    public AcademicService(FacultyRepository facultyRepository, AcademicProgramRepository academicProgramRepository) {
        this.facultyRepository = facultyRepository;
        this.academicProgramRepository = academicProgramRepository;
    }

    public Faculty createFaculty(Long id, String name) {
        this.facultyRepository.findByName(name).ifPresent(faculty -> {
            throw new DataAlreadyExistsException(MessagesConstants.FACULTY_ALREADY_EXISTS);
            
        });
        Faculty faculty = Faculty.builder()
                .id(id)
                .name(name)
                .build();
        return facultyRepository.save(faculty);
    }

    public AcademicProgram createAcademicProgram(Long id, String name, Long facultyId) {
        this.academicProgramRepository.findById(id).ifPresent(academicProgram -> {
            throw new DataAlreadyExistsException(MessagesConstants.ACADEMIC_PROGRAM_ALREADY_EXISTS);
        });
        AcademicProgram academicProgram = AcademicProgram.builder()
                .id(id)
                .name(name)
                .faculty(facultyRepository.findById(facultyId).orElseThrow(() -> new DataNotFoundException(MessagesConstants.FACULTY_NOT_FOUND)))
                .build();
        return academicProgramRepository.save(academicProgram);
    }
}
