package co.udea.codefact.administration;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.udea.codefact.appointment.AppointmentService;
import co.udea.codefact.academic.AcademicProgramDTO;
import co.udea.codefact.academic.AcademicService;
import co.udea.codefact.academic.FacultyDTO;
import co.udea.codefact.appointment.AppointmentAllDataDTO;
import co.udea.codefact.appointment.AppointmentDTO;
import co.udea.codefact.appointment.AppointmentDataCSV;
import co.udea.codefact.professor.ProfessorDTO;
import co.udea.codefact.professor.ProfessorService;
import co.udea.codefact.subject.Subject;
import co.udea.codefact.subject.SubjectRequestDTO;
import co.udea.codefact.subject.SubjectResponseDTO;
import co.udea.codefact.subject.SubjectService;
import co.udea.codefact.subject.SubjectUpdateDTO;
import co.udea.codefact.tutor.TutorDTO;
import co.udea.codefact.tutor.TutorService;
import co.udea.codefact.user.User;
import co.udea.codefact.user.UserChangeRoleDTO;
import co.udea.codefact.user.UserDTO;
import co.udea.codefact.user.UserMapper;
import co.udea.codefact.user.UserRole;
import co.udea.codefact.user.UserRoleChangeKey;
import co.udea.codefact.user.UserRoleDTO;
import co.udea.codefact.user.UserRoleService;
import co.udea.codefact.user.UserService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.InvalidRoleChangeException;


@Service
public class AdministrationService {

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final TutorService tutorService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final AppointmentService appointmentService;
    private final AcademicService academicService;

    public AdministrationService(
                            UserService userService, 
                            UserRoleService userRoleService, 
                            TutorService tutorService, 
                            ProfessorService professorService, 
                            SubjectService subjectService,
                            AppointmentService appointmentService,
                            AcademicService academicService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.tutorService = tutorService;
        this.professorService = professorService;
        this.subjectService = subjectService;
        this.appointmentService = appointmentService;
        this.academicService = academicService;
    }
    
    public List<UserDTO> getUsersByRole(Long roleId) {
        List<UserDTO> listStudents = new ArrayList<>();
        for (User student : this.userService.getUsersByRole(roleId)) {
            listStudents.add(UserMapper.toUserDTO(student));
        }
        return listStudents;
    }

    public List<UserDTO> getUsersByName(String name) {
        List<UserDTO> listStudents = new ArrayList<>();
        for (User student : this.userService.getUsersByName(name.toLowerCase())) {
            listStudents.add(UserMapper.toUserDTO(student));
        }
        return listStudents;
    }

    public AppointmentAllDataDTO getAppointmentByIdAsAdmin(Long appointmentId) {
        return this.appointmentService.getAppointmentByIdAsAdmin(appointmentId);
    }
    
    public List<AppointmentDTO> getAllAppointments(){
        return this.appointmentService.getAllAppointments();
    }

    public String appointmentsListToCSVFile(){
        List<AppointmentDataCSV> listAppointmentsCsvs = this.appointmentService.getAllAppointmentsToCSV();
        StringWriter writer = new StringWriter();
        writer.append("id,date,is_virtual,appointment_status,subject_id,academic_program_id,calification\n");
        for (AppointmentDataCSV appointmentDataCSV : listAppointmentsCsvs) {
            writer.append(String.format("%d,%s,%b,%s,%d,%d,%d\n",
                appointmentDataCSV.getId(),
                appointmentDataCSV.getDate(),
                appointmentDataCSV.getIsVirtual(),
                appointmentDataCSV.getAppointmentStatus(),
                appointmentDataCSV.getSubjectId(),
                appointmentDataCSV.getAcademicProgramId(),
                appointmentDataCSV.getCalification()));
        }
        return writer.toString();
    }

    
    public SubjectResponseDTO createSubject(SubjectRequestDTO subject){
        return this.subjectService.createSubject(subject);
    }

    public List<UserRoleDTO> getRoles() {
        return this.userRoleService.getRoles();
    }

    public void assignSubjectToTutor(AssignSubjectDTO tutorSubjectDTO){
        User user = this.userService.getUserByUsername(tutorSubjectDTO.getUsername());
        Subject subject = this.subjectService.getSubjectByCode(tutorSubjectDTO.getSubjectCode());
        this.tutorService.assignSubject(user, subject);
    }

    public void unassignSubjectToTutor(String username){
        User user = this.userService.getUserByUsername(username);
        this.tutorService.unassignSubject(user);
    }

    public void assignSubjectToProfessor(AssignSubjectDTO tutorSubjectDTO){
        User user = this.userService.getUserByUsername(tutorSubjectDTO.getUsername());
        Subject subject = this.subjectService.getSubjectByCode(tutorSubjectDTO.getSubjectCode());
        this.professorService.assignSubject(user, subject);
    }

    public SubjectResponseDTO updateSubject(SubjectUpdateDTO subjectDTO) {
        return this.subjectService.updateSubject(subjectDTO);
    }

    public TutorDTO getTutorInfo(String username) {
        return this.tutorService.getTutorDTO(username.toLowerCase());
    }

    public ProfessorDTO getProfessorInfo(String username) {
        return this.professorService.getProfessorDTO(username.toLowerCase());
    }

    public void deleteProfessorSubject(Long idProfessor) {
        this.professorService.deleteProfessorSubject(idProfessor);
    }

    public void createAcademicProgram(AcademicProgramDTO academicProgramDTO) {
        this.academicService.createAcademicProgram(academicProgramDTO);
    }

    public void createFaculty(FacultyDTO facultyDTO) {
        this.academicService.createFaculty(facultyDTO);
    }
    
    public UserDTO changeUserRole(UserChangeRoleDTO userChangeRoleDTO) {
        
        User user = this.userService.getUserByUsername(userChangeRoleDTO.getUsername());
        Long oldRoleId = this.userRoleService.findRoleById(user.getRole().getId()).getId();
        
        UserRole newRole = this.userRoleService.findRoleById(userChangeRoleDTO.getIdRole());
        Long newRoleId = this.userRoleService.findRoleById(userChangeRoleDTO.getIdRole()).getId();
        
        if (oldRoleId.equals(newRoleId)) {
            return UserMapper.toUserDTO(user);
        }

        Map<UserRoleChangeKey, Runnable> userRoleChange = new HashMap<>();
        final User finalUser = user;

        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.STUDENT_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.TUTOR_ID, RoleConstants.STUDENT_ID), () -> this.tutorToStudent(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.STUDENT_ID, RoleConstants.TUTOR_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.TUTOR_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.PROFESSOR_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.ADMIN_ID, RoleConstants.PROFESSOR_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.ADMIN_ID), () -> this.changeRole(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.PROFESSOR_ID, RoleConstants.ADMIN_ID), () -> this.changeRole(finalUser, newRole));

        UserRoleChangeKey userRoleChangeKey = new UserRoleChangeKey(oldRoleId, newRoleId);
        userRoleChange.getOrDefault(userRoleChangeKey, () -> { 
            throw new InvalidRoleChangeException(MessagesConstants.INVALID_ROLE_CHANGE); }).run();

        return UserMapper.toUserDTO(this.userService.getUserByUsername(user.getUsername()));
    }
    
    private void changeRole(User user, UserRole newRole) {
        user.setRole(newRole);
        this.userService.saveUser(user);
    }
    
    private void tutorToStudent(User user, UserRole newRole) {
        this.tutorService.disableTutor(user);
        user.setRole(newRole);
        this.userService.saveUser(user);
    }

}
