package co.udea.codefact.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class UserRoleService {
    
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole findRoleByRole(String role) {
        return this.userRoleRepository.findByRole(role);
    }

    public UserRole findRoleById(Long id) {
        return this.userRoleRepository.findById(id).orElseThrow(() -> new DataNotFoundException(MessagesConstants.USER_ROLE_NOT_FOUND));
    }

    public void createRoleIfNotExists(Long id, String role) {
        if (!userRoleRepository.existsByRole(role)) {
            UserRole userRole = UserRole.builder()
                .id(id)
                .role(role)
                .build();
            this.userRoleRepository.save(userRole);
        }
    }

    public List<UserRoleDTO> getRoles() {
        List<UserRoleDTO> roles = new ArrayList<>();
        for (UserRole userRole : this.userRoleRepository.findAll()) {
            roles.add(UserRoleMapper.toUserRoleDTO(userRole));
        }
        return roles;
    }
}
