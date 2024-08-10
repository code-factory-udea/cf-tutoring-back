package co.udea.codefact.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import co.udea.codefact.login.LoginLDAPResponse;
import co.udea.codefact.professor.ProfessorService;
import co.udea.codefact.tutor.TutorService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.InvalidRoleChangeException;
import co.udea.codefact.utils.exceptions.UserNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final TutorService tutorService;
    private final ProfessorService professorService;


    public UserService(UserRepository userRepository, UserRoleService userRoleService, TutorService tutorService, ProfessorService professorService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.tutorService = tutorService;
        this.professorService = professorService;
    }

    public List<UserDTO> getUsersByRole(Long roleId) {
        List<User> students = this.userRepository.findAllByRoleId(roleId);
        List<UserDTO> listStudents = new ArrayList<>();
        for (User student : students) {
            listStudents.add(UserMapper.toUserDTO(student));
        }
        return listStudents;
    }

    public List<UserDTO> getUsersByName(String name) {
        List<User> students = this.userRepository.findByFirstNameOrLastNameContaining(name.toLowerCase());
        List<UserDTO> listStudents = new ArrayList<>();
        for (User student : students) {
            listStudents.add(UserMapper.toUserDTO(student));
        }
        return listStudents;
    }

    public User loginUser(LoginLDAPResponse loginLDAPResponse) {
        Optional<User> user = this.userRepository.findByUsername(loginLDAPResponse.getUser());
        if (!user.isPresent()) {
            return this.saveUser(loginLDAPResponse);
        }
        return this.userRepository.findByUsername(loginLDAPResponse.getUser()).get();
    }

    private User saveUser(LoginLDAPResponse LDAPResponse) {
            User user = User.builder()  
                .username(LDAPResponse.getUser())
                .firstName(LDAPResponse.getName())
                .lastName(LDAPResponse.getLastName())
                .role(this.getRole(LDAPResponse.getRole()))
                .build();
            return this.userRepository.save(user);
    }

    private UserRole getRole(String role) {
        switch (role) {
            case "estudiante":
            case "auxProg":
            case "auxAdmin":
                role = RoleConstants.STUDENT;
                break;
            case "profesor":
                role = RoleConstants.PROFESSOR;
                break;
            default:
                role = RoleConstants.UNKNOWN;
                break;
        }
        return this.userRoleService.findRoleByRole(role);
    }

    public UserDTO changeUserRole(UserChangeRoleDTO userChangeRoleDTO) {
        System.out.println("nuevo rol: "+userChangeRoleDTO.getIdRole());
        //User user = this.userRepository.findByUsername(userChangeRoleDTO.getUsername()).get();
        User user = this.userRepository.findByUsername(userChangeRoleDTO.getUsername()).orElseThrow(() -> new UserNotFoundException(MessagesConstants.USER_NOT_FOUND));
        Long oldRoleId = this.userRoleService.findRoleById(user.getRole().getId()).getId();
        System.out.println("oldRoleId: "+oldRoleId);
        
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
            System.out.println("Entré a la excepción");
            throw new InvalidRoleChangeException(MessagesConstants.INVALID_ROLE_CHANGE); }).run();

        return UserMapper.toUserDTO(this.userRepository.findByUsername(user.getUsername()).get());
    }

    private void changeRole(User user, UserRole newRole) {
        System.out.println("Entré a changeRole");
        user.setRole(newRole);
        System.out.println("Nuevo rol: "+user.getRole().getRole());
        this.userRepository.save(user);
    }
    
    private void changeToTutor(User user, UserRole newRole) {
        System.out.println("Entré a changeToTutor");
        this.tutorService.enableTutor(user);
        user.setRole(newRole);
        this.userRepository.save(user);
    }

    private void tutorToStudent(User user, UserRole newRole) {
        System.out.println("Entré a tutorToStudent");
        this.tutorService.disableTutor(user);
        user.setRole(newRole);
        this.userRepository.save(user);
    }

    private void changeToProfessor(User user, UserRole newRole) {
        System.out.println("Entré a changeToProfessor");
        this.professorService.createProfessor(user);
        user.setRole(newRole);
        this.userRepository.save(user);
    }

}



