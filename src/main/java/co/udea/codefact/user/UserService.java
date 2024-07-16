package co.udea.codefact.user;

import java.util.Optional;

import org.springframework.stereotype.Service;
import co.udea.codefact.login.LoginLDAPResponse;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole findRoleByRole(String role) {
        return userRoleRepository.findByRole(role);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
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
            return userRepository.save(user);
    }

    private UserRole getRole(String role) {
        switch (role) {
            case "estudiante":
            case "auxProg":
            case "auxAdmin":
                role = "Estudiante";
                break;
            case "Profesor":
                role = "Profesor";
                break;
            default:
                role = "No identificado";
                break;
        }
        return this.findRoleByRole(role);
    }
    

}
