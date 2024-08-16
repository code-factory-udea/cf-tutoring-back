package co.udea.codefact.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.academic.AcademicProgram;
import co.udea.codefact.academic.AcademicService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataAlreadyExistsException;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final AcademicService academicService;
    
    public SubjectService(SubjectRepository subjectRepository, AcademicService academicService) {
        this.subjectRepository = subjectRepository;
        this.academicService = academicService;
    }

    public SubjectResponseDTO createSubject(SubjectRequestDTO subjectDTO) {
        Optional<Subject> subjectOptional = this.subjectRepository.findByCode(subjectDTO.getCode());
        if (subjectOptional.isPresent()) {
            throw new DataAlreadyExistsException(MessagesConstants.SUBJECT_ALREADY_EXISTS);
        }
        AcademicProgram academic = this.academicService.getAcademicProgram(subjectDTO.getAcademicProgramId());
        Subject subject = Subject.builder().code(subjectDTO.getCode()).name(subjectDTO.getName()).academicProgram(academic).build();
        return SubjectMapper.toDTO(this.subjectRepository.save(subject));
    }
    
    public List<SubjectListDTO> getSubjects() {
        List<SubjectListDTO> subjects = new ArrayList<>(); ;
        for (Subject subject : this.subjectRepository.findAll()) {
            subjects.add(SubjectMapper.toListDTO(subject));
        }
        return subjects;
    }

    public Subject getSubject(Long id) {
        return this.subjectRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.SUBJECT_NOT_FOUND));
    }

    public Subject getSubjectByCode(Long code) {
        return this.subjectRepository.findByCode(code).orElseThrow(() -> new DataNotFoundException(MessagesConstants.SUBJECT_NOT_FOUND));
    }
    
    public SubjectResponseDTO updateSubject(SubjectRequestDTO subjectDTO) {
        Subject subject = this.getSubjectByCode(subjectDTO.getCode());
        if (subjectDTO.getName() != null) {
            subject.setName(subjectDTO.getName());
        }
        if (subjectDTO.getAcademicProgramId() != null) {
            AcademicProgram academic = this.academicService.getAcademicProgram(subjectDTO.getAcademicProgramId());
            subject.setAcademicProgram(academic);
        }
        return SubjectMapper.toDTO(this.subjectRepository.save(subject));
    }


}
