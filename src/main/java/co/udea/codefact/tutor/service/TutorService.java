package co.udea.codefact.tutor.service;

import java.util.Optional;

import co.udea.codefact.tutor.dto.TutorDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.repository.TutorRepository;
import org.springframework.stereotype.Service;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.subject.service.SubjectService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final TutorScheduleService tutorScheduleService;
    private final SubjectService subjectService;

    public TutorService(
            TutorRepository tutorRepository,
            TutorScheduleService tutorScheduleService,
            SubjectService subjectService) {
        this.tutorRepository = tutorRepository;
        this.tutorScheduleService = tutorScheduleService;
        this.subjectService = subjectService;
    }

    public Tutor getTutorById(Long id) {
        return this.tutorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
    }

    public Tutor getTutorByUser(User user) {
        return this.tutorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
    }

    public void disableTutor(User user) {
        Tutor tutor = this.getTutorByUser(user);
        tutor.setIsActive(false);
        this.tutorRepository.save(tutor);
        this.tutorScheduleService.deleteTutorSchedules(tutor);
    }

    public void assignSubject(User user, Subject subject) {
        if (!user.getRole().getRole().equals(RoleConstants.TUTOR)) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);
        }
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (tutor.isPresent()) {
            tutor.get().setIsActive(true);
            tutor.get().setSubject(subject);
            this.tutorRepository.save(tutor.get());
            return;
        }
        Tutor newTutor = Tutor.builder().user(user).isActive(true).subject(subject).build();
        this.tutorRepository.save(newTutor);
    }

    public void unassignSubject(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (!tutor.isPresent()) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);
        }
        if (tutor.get().getSubject() == null) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND_OR_UNASSIGNED);
        }
        tutor.get().setSubject(null);
        this.tutorRepository.save(tutor.get());
    }

    public TutorDTO getTutorDTO(String username) {
        Tutor tutor = this.tutorRepository.findByUserUsername(username)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND_OR_UNASSIGNED));
        User user = tutor.getUser();
        TutorDTO.TutorDTOBuilder tutorDTOBuilder = TutorDTO.builder()
                .id(tutor.getId())
                .name(String.format(FormatConstants.FULLNAME_FORMAT, user.getFirstName(), user.getLastName()))
                .username(user.getUsername());
        this.getAditionalInfo(tutorDTOBuilder, tutor.getSubject());
        return tutorDTOBuilder.build();
    }

    private void getAditionalInfo(TutorDTO.TutorDTOBuilder builder, Subject subject) {
        if (subject == null) {
            builder.subjectInfo(MessagesConstants.NO_DATA);
            builder.academicProgramInfo(MessagesConstants.NO_DATA);
            return;
        }
        builder.subjectInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getCode(), subject.getName()));
        builder.academicProgramInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT,
                subject.getAcademicProgram().getName(), subject.getAcademicProgram().getFaculty().getName()));
    }
}
