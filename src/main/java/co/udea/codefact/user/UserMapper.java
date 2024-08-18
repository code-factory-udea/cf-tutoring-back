package co.udea.codefact.user;

import co.udea.codefact.utils.constants.FormatConstants;

public class UserMapper {
    
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
            .name(String.format(FormatConstants.FULLNAME_FORMAT, user.getFirstName(), user.getLastName()))
            .role(user.getRole().getRole())
            .build();
    }

    
}
