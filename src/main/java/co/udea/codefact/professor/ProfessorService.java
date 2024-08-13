package co.udea.codefact.professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.subject.Subject;
import co.udea.codefact.user.User;
import co.udea.codefact.user.UserService;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class ProfessorService {
    
    private final ProfessorRepository professorRepository;
    private final UserService userService;

    public ProfessorService(ProfessorRepository professorRepository, UserService userService) {
        this.professorRepository = professorRepository;
        this.userService = userService;
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
        User user = this.userService.getUserByUsername(username);
        Professor professor = this.getProfessorByUserId(user.getId());
        return ProfessorDTO.builder()
                .id(professor.getId())
                .name(String.format(FormatConstants.FULLNAME_FORMAT,professor.getUser().getFirstName(), professor.getUser().getLastName()) )
                .username(professor.getUser().getUsername())
                .professorSubjectInfo(this.getProfessorSubjectInfo(professor))
                .build();
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
}
