package co.udea.codefact.user.dto;

import co.udea.codefact.utils.constants.ConstraintsConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserChangeRoleDTO {

    @NotNull(message = ConstraintsConstants.USERNAME_NOT_NULL)
    @Size(min = 1, max = 50, message = ConstraintsConstants.USERNAME_SIZE)
    private String username;

    @NotNull(message = ConstraintsConstants.USER_ROLE_NOT_NULL)
    @Min(1)
    @Max(5)
    private Long idRole;
}
