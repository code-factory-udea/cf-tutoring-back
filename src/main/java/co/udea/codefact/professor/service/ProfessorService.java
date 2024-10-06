package co.udea.codefact.professor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.udea.codefact.professor.dto.ProfessorDTO;
import co.udea.codefact.professor.dto.ProfessorSubjectInfoDTO;
import co.udea.codefact.professor.entity.Professor;
import co.udea.codefact.professor.repository.ProfessorRepository;
import co.udea.codefact.utils.constants.RoleConstants;
import org.springframework.stereotype.Service;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class ProfessorService {
    
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public void assignSubject(User user, Subject subject) {
        if (!user.getRole().getRole().equals(RoleConstants.PROFESSOR)) {
            throw new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND);
        }
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

    public ProfessorDTO getProfessorDTO(User user) {
        Optional<Professor> professor = this.professorRepository.findFirstByUserUsername(user.getUsername());
        ProfessorDTO.ProfessorDTOBuilder professorDTOBuilder = ProfessorDTO.builder()
                .name(String.format(FormatConstants.FULLNAME_FORMAT,user.getFirstName(), user.getLastName()) )
                .username(user.getUsername());
        if (professor.isPresent()) {
            professorDTOBuilder.professorSubjectInfoDTO(this.getProfessorSubjectInfo(professor.get()));
            return professorDTOBuilder.build();
        }
        professorDTOBuilder.professorSubjectInfoDTO(this.getProfessorSubjectInfo(null));
        return professorDTOBuilder.build();
    }

    private List<ProfessorSubjectInfoDTO> getProfessorSubjectInfo(Professor professor) {
        List<ProfessorSubjectInfoDTO> professorSubjectInfoDTO = new ArrayList<>();
        if (professor == null) {
            professorSubjectInfoDTO.add(this.getSubjectInfo(ProfessorSubjectInfoDTO.builder(), null, null));
            return professorSubjectInfoDTO;
        }
        List<Professor> professors = this.professorRepository.findAllByUserId(professor.getUser().getId());
        for (Professor p : professors) {
            professorSubjectInfoDTO.add(this.getSubjectInfo(ProfessorSubjectInfoDTO.builder(), p.getId(), p.getSubject()));
        }
        return professorSubjectInfoDTO;
    }

    private ProfessorSubjectInfoDTO getSubjectInfo(ProfessorSubjectInfoDTO.ProfessorSubjectInfoDTOBuilder builder, Long idProfessor, Subject subject) {
        if (subject == null) {
            return builder
                    .idProfessor(null)
                    .subjectInfo(MessagesConstants.NO_DATA)
                    .academicProgramInfo(MessagesConstants.NO_DATA)
                    .build();
        }
        return builder
                .idProfessor(idProfessor)
                .subjectInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getCode(), subject.getName()))
                .academicProgramInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getAcademicProgram().getName(), subject.getAcademicProgram().getFaculty().getName()))
                .build();
    }

    public Professor getProfessorById(Long id) {
        return this.professorRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }

    public void deleteProfessorSubject(Long idProfessor) {
        Professor professor = this.getProfessorById(idProfessor);
        this.professorRepository.delete(professor);
    }
}
