package co.udea.codefact.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import co.udea.codefact.login.LoginLDAPResponse;
import co.udea.codefact.professor.ProfessorService;
import co.udea.codefact.tutor.TutorService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ProfessorService professorService;
    private final TutorService tutorService;


    public UserService(UserRepository userRepository, UserRoleService userRoleService, ProfessorService professorService, TutorService tutorService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.professorService = professorService;
        this.tutorService = tutorService;
    }

    public List<User> getUsersByRole(Long roleId) {
        return this.userRepository.findAllByRoleId(roleId);
    }

    public List<User> getUsersByName(String name) {
        return this.userRepository.findByFirstNameOrLastNameContaining(name.toLowerCase());
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException(MessagesConstants.USER_NOT_FOUND));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public User loginUser(LoginLDAPResponse loginLDAPResponse) {
        Optional<User> user = this.userRepository.findByUsername(loginLDAPResponse.getUser());
        if (!user.isPresent()) {
            return this.saveUser(loginLDAPResponse);
        }
        return this.userRepository.findByUsername(loginLDAPResponse.getUser()).get();
    }

    private User saveUser(LoginLDAPResponse ldapResponse) {
            User user = User.builder()  
                .username(ldapResponse.getUser())
                .firstName(ldapResponse.getName())
                .lastName(ldapResponse.getLastName())
                .role(this.getRole(ldapResponse.getRole()))
                .build();
            user = this.userRepository.save(user);
            this.createTutorOrProfessor(user);
            return user;
    }

    private UserRole getRole(String role) {
        switch (role) {
            case "estudiante", "auxProg", "auxAdmin":
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

    private void createTutorOrProfessor(User user) {
        if (user.getRole().getRole().equals(RoleConstants.TUTOR)) {
            this.tutorService.enableTutor(user);
        } else if (user.getRole().getRole().equals(RoleConstants.PROFESSOR)) {
            this.professorService.createProfessor(user);
        }
    }

}




