package co.udea.codefact.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;

@RestController
@RequestMapping(EndpointConstants.USER_ROLE)
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping(EndpointConstants.BASE)
    public ResponseEntity<List<UserRoleDTO>> getRoles() {
        return new ResponseEntity<>(this.userRoleService.getRoles(), null, 200);
    }
    
}
