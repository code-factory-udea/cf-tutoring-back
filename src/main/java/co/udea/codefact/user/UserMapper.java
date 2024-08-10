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

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
            .username(user.getUsername())
            .name(String.format("%s %s", user.getFirstName(), user.getLastName()))
            .role(user.getRole().getRole())
            .build();
    }

    
}
