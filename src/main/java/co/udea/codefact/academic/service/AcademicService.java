package co.udea.codefact.academic.service;

import java.util.ArrayList;
import java.util.List;

import co.udea.codefact.academic.repository.AcademicProgramRepository;
import co.udea.codefact.academic.repository.FacultyRepository;
import co.udea.codefact.academic.dto.AcademicProgramDTO;
import co.udea.codefact.academic.dto.AcademicProgramResponseDTO;
import co.udea.codefact.academic.dto.FacultyDTO;
import co.udea.codefact.academic.dto.FacultyResponseDTO;
import co.udea.codefact.academic.entity.AcademicProgram;
import co.udea.codefact.academic.entity.Faculty;
import co.udea.codefact.academic.utils.AcademicMapper;
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

    public Faculty createFaculty(FacultyDTO facultyDTO) {
        this.facultyRepository.findByName(facultyDTO.getName()).ifPresent(faculty -> {
            throw new DataAlreadyExistsException(MessagesConstants.FACULTY_ALREADY_EXISTS);
            
        });
        Faculty faculty = Faculty.builder()
                .name(facultyDTO.getName())
                .build();
        return facultyRepository.save(faculty);
    }

    public AcademicProgram createAcademicProgram(AcademicProgramDTO academicProgramDTO) {
        this.academicProgramRepository.findById(academicProgramDTO.getId()).ifPresent(academicProgram -> {
            throw new DataAlreadyExistsException(MessagesConstants.ACADEMIC_PROGRAM_ALREADY_EXISTS);
        });
        AcademicProgram academicProgram = AcademicProgram.builder()
                .id(academicProgramDTO.getId())
                .name(academicProgramDTO.getName())
                .faculty(this.getFaculty(academicProgramDTO.getFacultyId()))
                .build();
        return academicProgramRepository.save(academicProgram);
    }

    public AcademicProgram getAcademicProgram(Long id) {
        return this.academicProgramRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.ACADEMIC_PROGRAM_NOT_FOUND));
    }

    public Faculty getFaculty(Long id) {
        return this.facultyRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.FACULTY_NOT_FOUND));
    }

    public List<AcademicProgramResponseDTO> getAcademicPrograms() {
        List<AcademicProgramResponseDTO> academicPrograms = new ArrayList<>();
        for (AcademicProgram academicProgram : this.academicProgramRepository.findAll()) {
            academicPrograms.add(AcademicMapper.toAcademicProgramResponseDTO(academicProgram));
        }
        return academicPrograms;
    }

    public List<FacultyResponseDTO> getFaculties() {
        List<FacultyResponseDTO> faculties = new ArrayList<>();
        for (Faculty faculty : this.facultyRepository.findAll()) {
            faculties.add(AcademicMapper.toFacultyResponseDTO(faculty));
        }
        return faculties;
    }
}
