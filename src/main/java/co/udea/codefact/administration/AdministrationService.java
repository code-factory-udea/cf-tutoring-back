package co.udea.codefact.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.udea.codefact.professor.ProfessorService;
import co.udea.codefact.subject.Subject;
import co.udea.codefact.subject.SubjectRequestDTO;
import co.udea.codefact.subject.SubjectResponseDTO;
import co.udea.codefact.subject.SubjectService;
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

    public AdministrationService(UserService userService, UserRoleService userRoleService, TutorService tutorService, ProfessorService professorService, SubjectService subjectService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.tutorService = tutorService;
        this.professorService = professorService;
        this.subjectService = subjectService;
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
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.STUDENT_ID, RoleConstants.TUTOR_ID), () -> this.changeToTutor(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.TUTOR_ID), () -> this.changeToTutor(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.UNKNOWN_ID, RoleConstants.PROFESSOR_ID), () -> this.changeToProfessor(finalUser, newRole));
        userRoleChange.put(new UserRoleChangeKey(RoleConstants.ADMIN_ID, RoleConstants.PROFESSOR_ID), () -> this.changeToProfessor(finalUser, newRole));
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
    
    private void changeToTutor(User user, UserRole newRole) {
        this.tutorService.enableTutor(user);
        user.setRole(newRole);
        this.userService.saveUser(user);
    }

    private void tutorToStudent(User user, UserRole newRole) {
        this.tutorService.disableTutor(user);
        user.setRole(newRole);
        this.userService.saveUser(user);
    }

    private void changeToProfessor(User user, UserRole newRole) {
        this.professorService.createProfessor(user);
        user.setRole(newRole);
        this.userService.saveUser(user);
    }

    public SubjectResponseDTO createSubject(SubjectRequestDTO subject){
        return this.subjectService.createSubject(subject);
    }

    public List<UserRoleDTO> getRoles() {
        return this.userRoleService.getRoles();
    }

    public void assignSubjectToTutor(AssignSubjectDTO tutorSubjectDTO){
        User user = this.userService.getUserByUsername(tutorSubjectDTO.getUsername());
        Subject subject = this.subjectService.getSubject(tutorSubjectDTO.getSubjectId());
        this.tutorService.assignSubject(user, subject);
    }

    public void assignSubjectToProfessor(AssignSubjectDTO tutorSubjectDTO){
        User user = this.userService.getUserByUsername(tutorSubjectDTO.getUsername());
        Subject subject = this.subjectService.getSubject(tutorSubjectDTO.getSubjectId());
        this.professorService.assignSubject(user, subject);
    }

}
