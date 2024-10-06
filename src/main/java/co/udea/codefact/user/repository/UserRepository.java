package co.udea.codefact.user.repository;

import java.util.List;
import java.util.Optional;

import co.udea.codefact.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

    List<User> findAllByRoleId(Long roleId);

    Page<User> findAllByRoleId(Long roleId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.id = :roleId AND ( LOWER(u.firstName) LIKE %:name% OR LOWER(u.lastName) LIKE %:name% )")
    Page<User> findByRoleIdAndFirstNameOrLastNameContains(@Param("roleId")Long roleId, Pageable pageable, @Param("name")String name);
}
