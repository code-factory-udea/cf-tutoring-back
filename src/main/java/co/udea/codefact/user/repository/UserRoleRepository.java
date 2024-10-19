package co.udea.codefact.user.repository;

import co.udea.codefact.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(String role);
    boolean existsByRole(String role);
    
}
