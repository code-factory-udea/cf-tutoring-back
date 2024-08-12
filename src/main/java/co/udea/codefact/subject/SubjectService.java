package co.udea.codefact.subject;

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
        Optional<Subject> subjectOptional = this.subjectRepository.findById(subjectDTO.getId());
        if (subjectOptional.isPresent()) {
            throw new DataAlreadyExistsException(MessagesConstants.SUBJECT_ALREADY_EXISTS);
        }
        AcademicProgram academic = this.academicService.getAcademicProgram(subjectDTO.getAcademicProgramId());
        Subject subject = Subject.builder().id(subjectDTO.getId()).name(subjectDTO.getName()).academicProgram(academic).build();
        return SubjectMapper.toDTO(this.subjectRepository.save(subject));
    }
    
    public List<SubjectResponseDTO> getSubjects() {
        //return SubjectMapper.toDTOList(this.subjectRepository.findAll());
        return null;
    }

    public Subject getSubject(Long id) {
        Optional<Subject> subject = this.subjectRepository.findById(id);
        if (!subject.isPresent()) {
            throw new DataNotFoundException(MessagesConstants.SUBJECT_NOT_FOUND);
        }
        return subject.get();
    }


}
