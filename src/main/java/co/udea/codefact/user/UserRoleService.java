package co.udea.codefact.user;

import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole findRoleByRole(String role) {
        return this.userRoleRepository.findByRole(role);
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
}
