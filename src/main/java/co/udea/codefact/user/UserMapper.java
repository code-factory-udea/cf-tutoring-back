package co.udea.codefact.user;

public class UserMapper {
    
    //Método estático
    public static UserDTO toLoginUserDTO(User user) {
        return UserDTO.builder()
            .username(user.getUsername())
            .name(user.getFirstName())
            .role(user.getRole().getRole())
            .build();
    }
}
