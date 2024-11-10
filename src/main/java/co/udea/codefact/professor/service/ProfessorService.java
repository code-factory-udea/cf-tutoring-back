package co.udea.codefact.professor.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.appointment.service.AppointmentService;
import co.udea.codefact.professor.dto.ProfessorDTO;
import co.udea.codefact.professor.dto.ProfessorSubjectInfoDTO;
import co.udea.codefact.professor.entity.Professor;
import co.udea.codefact.professor.repository.ProfessorRepository;
import co.udea.codefact.subject.dto.SubjectListDTO;
import co.udea.codefact.tutor.dto.TutorListDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.utils.auth.AuthenticationUtil;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataAlreadyExistsException;
import org.springframework.stereotype.Service;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final AuthenticationUtil authenticationUtil;
    private final TutorService tutorService;
    private final AppointmentService appointmentService;

    public ProfessorService(ProfessorRepository professorRepository,
                            AuthenticationUtil authenticationUtil,
                            TutorService tutorService,
                            AppointmentService appointmentService) {
        this.professorRepository = professorRepository;
        this.authenticationUtil = authenticationUtil;
        this.tutorService = tutorService;
        this.appointmentService = appointmentService;
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
        if (this.professorRepository.existsByUserUsernameAndSubjectId(user.getUsername(), subject.getId())) {
            throw new DataAlreadyExistsException(MessagesConstants.PROFESSOR_SUBJECT_ALREADY_EXISTS);
        }
        Professor newProfessor = Professor.builder().user(user).subject(subject).build();
        this.professorRepository.save(newProfessor);
    }

    public Professor getProfessorByUserId(Long userId) {
        return this.professorRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }

    public ProfessorDTO getProfessorDTO(User user) {
        Optional<Professor> professor = this.professorRepository.findFirstByUserUsername(user.getUsername());
        ProfessorDTO.ProfessorDTOBuilder professorDTOBuilder = ProfessorDTO.builder()
                .name(String.format(FormatConstants.FULLNAME_FORMAT, user.getFirstName(), user.getLastName()))
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
        for (Professor item : professors) {
            professorSubjectInfoDTO.add(
                    this.getSubjectInfo(ProfessorSubjectInfoDTO.builder(), item.getId(), item.getSubject())
            );
        }
        return professorSubjectInfoDTO;
    }

    private ProfessorSubjectInfoDTO getSubjectInfo(ProfessorSubjectInfoDTO.ProfessorSubjectInfoDTOBuilder builder,
                                                   Long idProfessor, Subject subject) {
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

    public Professor getProfessorByUsername(String username) {
        return this.professorRepository.findFirstByUserUsername(username)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }

    public void deleteProfessorSubject(Long idProfessor) {
        Professor professor = this.getProfessorById(idProfessor);
        this.professorRepository.delete(professor);
    }

    public List<SubjectListDTO> getProfessorSubject() {
        Professor professorAuthenticated = this.getProfessorByUsername(getUser());
        User userProfessor = professorAuthenticated.getUser();
        List<Professor> listProfessor = this.professorRepository.findAllByUserId(userProfessor.getId());
        List<SubjectListDTO> subjectList = new ArrayList<>();
        for (Professor professor : listProfessor ){
            Subject subject = professor.getSubject();
            subjectList.add(
                    SubjectListDTO.builder().code(subject.getCode()).name(subject.getName()).build()
            );
        }
        return subjectList;
    }

    public List<TutorListDTO> getTutorsBySubject(Long subjectCode){
        String username = this.getUser();
        Professor professorAuthenticated = this.getProfessorByUsernameAndSubject(username, subjectCode);
        return this.tutorService.getTutorsBySubject(professorAuthenticated.getSubject().getCode());
    }

    public List<AppointmentDTO> getTutorsCompletedAppointments(String username, LocalDate initialDate, LocalDate finalDate){
        Tutor tutor = this.tutorService.getTutorByUsername(username).orElseThrow(() -> new
                DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
        return this.appointmentService.getAllAppointmentsByTutorAndDate(tutor, initialDate, finalDate.plusDays(1));
    }

    public Professor getProfessorByUsernameAndSubject(String username, Long subjectCode){

        return this.professorRepository.findByUserUsernameAndSubjectCode(username, subjectCode)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.PROFESSOR_NOT_FOUND));
    }
    private String getUser() {
        return this.authenticationUtil.getAuthenticatedUser();
    }
}
