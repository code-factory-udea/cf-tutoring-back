package co.udea.codefact.user.utils;


import co.udea.codefact.user.entity.UserRole;
import co.udea.codefact.user.dto.UserRoleDTO;

public class UserRoleMapper {
    
    public static UserRoleDTO toUserRoleDTO(UserRole userRole) {
        return UserRoleDTO.builder()
            .id(userRole.getId())
            .role(userRole.getRole())
            .build();
    }

    private UserRoleMapper(){
        throw new IllegalStateException("Utility class");
    }
}
