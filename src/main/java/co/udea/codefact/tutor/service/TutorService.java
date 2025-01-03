package co.udea.codefact.tutor.service;

import java.util.List;
import java.util.Optional;

import co.udea.codefact.tutor.dto.*;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import co.udea.codefact.tutor.repository.TutorRepository;
import co.udea.codefact.tutor.utils.TutorMapper;
import co.udea.codefact.tutor.utils.TutorScheduleMapper;
import co.udea.codefact.utils.auth.AuthenticationUtil;
import org.springframework.stereotype.Service;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class TutorService {

    private final TutorRepository tutorRepository;
    private final AuthenticationUtil authenticationUtil;
    private final TutorScheduleService tutorScheduleService;

    public TutorService(
            TutorRepository tutorRepository,
            TutorScheduleService tutorScheduleService,
            AuthenticationUtil authenticationUtil) {
        this.tutorRepository = tutorRepository;
        this.authenticationUtil = authenticationUtil;
        this.tutorScheduleService = tutorScheduleService;
    }

    public Tutor getTutorById(Long id) {
        return this.tutorRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
    }

    public Optional<Tutor> getTutorByUser(User user) {
        return this.tutorRepository.findByUserId(user.getId());
    }

    public Optional<Tutor> getTutorByUsername(String username) {
        return this.tutorRepository.findByUserUsername(username);
    }

    public void disableTutor(User user) {
        Optional<Tutor> tutor = this.getTutorByUser(user);
        if (tutor.isPresent()) {
            tutor.get().setActive(false);
            this.tutorRepository.save(tutor.get());
            this.tutorScheduleService.deleteTutorSchedules(tutor.get());
        }
    }

    public void assignSubject(User user, Subject subject) {
        if (!user.getRole().getRole().equals(RoleConstants.TUTOR)) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);
        }
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (tutor.isPresent()) {
            tutor.get().setActive(true);
            tutor.get().setSubject(subject);
            this.tutorRepository.save(tutor.get());
            return;
        }
        Tutor newTutor = Tutor.builder().user(user).isActive(true).subject(subject).build();
        this.tutorRepository.save(newTutor);
    }

    public void unassignSubject(Long id) {
        Optional<Tutor> tutor = this.tutorRepository.findById(id);
        if (tutor.isEmpty()) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);
        }
        if (tutor.get().getSubject() == null) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND_OR_UNASSIGNED);
        }
        tutor.get().setSubject(null);
        this.tutorRepository.save(tutor.get());
    }

    public TutorDTO getTutorDTO(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserUsername(user.getUsername());
        TutorDTO.TutorDTOBuilder tutorDTOBuilder = TutorDTO.builder()
                .name(String.format(FormatConstants.FULLNAME_FORMAT, user.getFirstName(), user.getLastName()))
                .username(user.getUsername());
        if (tutor.isPresent()) {
            tutorDTOBuilder.id(tutor.get().getId());
            this.getAdditionalInfo(tutorDTOBuilder, tutor.get().getSubject());
            return tutorDTOBuilder.build();
        }
        this.getAdditionalInfo(tutorDTOBuilder, null);
        return tutorDTOBuilder.build();
    }

    private void getAdditionalInfo(TutorDTO.TutorDTOBuilder builder, Subject subject) {
        if (subject == null) {
            builder.subjectInfo(MessagesConstants.NO_DATA);
            builder.academicProgramInfo(MessagesConstants.NO_DATA);
            return;
        }
        builder.subjectInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT, subject.getCode(), subject.getName()));
        builder.academicProgramInfo(String.format(FormatConstants.ACADEMIC_INFO_FORMAT,
                subject.getAcademicProgram().getName(), subject.getAcademicProgram().getFaculty().getName()));
    }

    public void createTutorSchedule(CreateTutorScheduleDTO scheduleDTO) {
        Tutor tutor = this.getTutorAuthenticated();
        this.tutorScheduleService.createTutorSchedule(scheduleDTO, tutor);
    }

    public TutorSchedule getTutorSchedule(Long scheduleId) {
        return this.tutorScheduleService.getTutorSchedule(scheduleId);
    }

    public List<TutorScheduleDTO> getTutorSchedules() {
        Tutor tutor = this.getTutorAuthenticated();
        return TutorScheduleMapper.toListDTO(this.tutorScheduleService.getTutorSchedules(tutor));
    }

    public List<TutorScheduleDTO> getTutorSchedules(String username) {
        Tutor tutor = this.getTutorByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_WITHOUT_SUBJECT));
        return TutorScheduleMapper.toListDTO(this.tutorScheduleService.getTutorSchedules(tutor));
    }

    public void deleteTutorSchedule(Long scheduleId) {
        Tutor tutor = this.getTutorAuthenticated();
        this.tutorScheduleService.deleteTutorSchedule(scheduleId, tutor);
    }

    public void assignVirtualLink(VirtualLinkDTO virtualLink){
        Tutor tutor = this.getTutorAuthenticated();
        tutor.setVirtualMeetingLink(virtualLink.getLink());
        this.tutorRepository.save(tutor);
    }
    public VirtualLinkDTO getVirtualLink(){
        Tutor tutor = this.getTutorAuthenticated();
        String link =  tutor.getVirtualMeetingLink() != null ? tutor.getVirtualMeetingLink() : "El link no ha sido asignado";
        return VirtualLinkDTO.builder().link(link).build();
    }

    public List<TutorListDTO> getTutorsBySubject(Long subjectId){
        List<Tutor> list = this.tutorRepository.findAllBySubjectCodeAndActivate(subjectId);
        return TutorMapper.toListDTO(list);
    }

    public List<TutorScheduleDTO> getTutorScheduleSlots(String username){
        Tutor tutor = this.getTutorByUsername(username).orElseThrow(
                () -> new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND));
        List< TutorSchedule> schedules = this.tutorScheduleService.getTutorSchedules(tutor);
        return TutorScheduleMapper.toSlotListDTO(schedules);
    }

    public Tutor getTutorAuthenticated(){
        return this.getTutorByUsername(this.getUser())
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_WITHOUT_SUBJECT));
    }

    private String getUser(){
        return this.authenticationUtil.getAuthenticatedUser();
    }
}
