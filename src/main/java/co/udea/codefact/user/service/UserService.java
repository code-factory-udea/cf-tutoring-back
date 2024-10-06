package co.udea.codefact.user.service;

import java.util.List;
import java.util.Optional;

import co.udea.codefact.user.entity.User;
import co.udea.codefact.user.entity.UserRole;
import co.udea.codefact.user.repository.UserRepository;
import co.udea.codefact.utils.constants.FormatConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import co.udea.codefact.login.dto.LoginLDAPResponse;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;


    public UserService(UserRepository userRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    public List<User> getUsersByRole(Long roleId) {
        return this.userRepository.findAllByRoleId(roleId);
    }

    public Page<User> getUsersByRole(Long roleId, int page, String name) {
        Pageable pageable = PageRequest.of(page, FormatConstants.ITEM_PER_PAGE);
        if (name != null && !name.isEmpty()) {
            return this.userRepository.findByRoleIdAndFirstNameOrLastNameContains(roleId, pageable, name);
        }
        return this.userRepository.findAllByRoleId(roleId, pageable);
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



}




