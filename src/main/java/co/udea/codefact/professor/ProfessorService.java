package co.udea.codefact.professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.subject.Subject;
import co.udea.codefact.user.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class ProfessorService {
    
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
        
    }

    public Professor createProfessor(User user) {
        Optional<Professor> professor = this.professorRepository.findByUserId(user.getId());
        if (!professor.isPresent()) {
            return this.professorRepository.save(Professor.builder().user(user).build());
        }
        return professor.get();
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public void assignSubject(User user, Subject subject) {
        Optional<Professor> professor = this.professorRepository.findTopByUserIdOrderByIdDesc(user.getId());
        if (professor.isPresent() && !professor.get().hasSubject()) {
            professor.get().setSubject(subject);
            this.professorRepository.save(professor.get());
            return;
        }
        Professor newProfessor = Professor.builder().user(user).subject(subject).build();
        this.professorRepository.save(newProfessor);
    }
    
    public Professor getProfessorByUserId(Long userId) {
        return this.professorRepository.findByUserId(userId).orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }

    public ProfessorDTO getProfessorDTO(String username) {
        Optional<Professor> professor = this.professorRepository.findFirstByUserUsername(username);
        if (professor.isPresent()) {
            User user = professor.get().getUser();
            return ProfessorDTO.builder()
                    .id(professor.get().getId())
                    .name(String.format(FormatConstants.FULLNAME_FORMAT,user.getFirstName(), user.getLastName()) )
                    .username(user.getUsername())
                    .professorSubjectInfo(this.getProfessorSubjectInfo(professor.get()))
                    .build();
        }
        throw new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND_OR_UNASSIGNED);
        
    }

    private List<ProfessorSubjectInfo> getProfessorSubjectInfo(Professor professor) {
        List<ProfessorSubjectInfo> professorSubjectInfo = new ArrayList<>();
        List<Professor> professors = this.professorRepository.findAllByUserId(professor.getUser().getId());
        for (Professor p : professors) {
            professorSubjectInfo.add(this.getSubjectInfo(ProfessorSubjectInfo.builder(), p.getSubject()));
        }
        return professorSubjectInfo;
    }

    private ProfessorSubjectInfo getSubjectInfo(ProfessorSubjectInfo.ProfessorSubjectInfoBuilder builder, Subject subject) {
        if (subject == null) {
            return builder
                    .subjectInfo(MessagesConstants.NO_DATA)
                    .academicProgramInfo(MessagesConstants.NO_DATA)
                    .build();
        }
        return builder
                .subjectInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getCode(), subject.getName()))
                .academicProgramInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getAcademicProgram().getName(), subject.getAcademicProgram().getFaculty().getName()))
                .build();

    }

    public Professor getProfessorById(Long id) {
        return this.professorRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }

    public void deleteProfessorSubject(ProfessorDeleteDTO professorDeleteDTO) {
        Professor professor = this.professorRepository.findByIdAndSubjectCode(professorDeleteDTO.getId(),professorDeleteDTO.getSubjectCode()).orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_SUBJECT_NOT_FOUND));
        User user = professor.getUser();
        this.professorRepository.delete(professor);
        if (this.professorRepository.findAllByUserId(professor.getUser().getId()).isEmpty()) {
            this.createProfessor(user);
        }
    }
}
