package co.udea.codefact.user;


public class UserRoleMapper {
    
    public static UserRoleDTO toUserRoleDTO(UserRole userRole) {
        return UserRoleDTO.builder()
            .id(userRole.getId())
            .role(userRole.getRole())
            .build();
    }
}
