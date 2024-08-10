package co.udea.codefact.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.InvalidRoleChangeException;


@RestController
@RequestMapping(EndpointConstants.USER)
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping(EndpointConstants.USER_ROLE)
    public ResponseEntity<UserDTO> changeRole(UserChangeRoleDTO userChangeRoleDTO) {
        return new ResponseEntity<>(this.userService.changeUserRole(userChangeRoleDTO), null, 200);
    }

    @ExceptionHandler(InvalidRoleChangeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleInvalidRoleChangeException(InvalidRoleChangeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }
    @GetMapping(EndpointConstants.STUDENT)
    public ResponseEntity<List<UserDTO>> getStudents() {
        return new ResponseEntity<>(this.userService.getUsersByRole(RoleConstants.STUDENT_ID), null, 200);
    }

    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<UserDTO>> getTutors() {
        return new ResponseEntity<>(this.userService.getUsersByRole(RoleConstants.TUTOR_ID), null, 200);
    }

    @GetMapping(EndpointConstants.PROFESSOR)
    public ResponseEntity<List<UserDTO>> getProfessors() {
        return new ResponseEntity<>(this.userService.getUsersByRole(RoleConstants.PROFESSOR_ID), null, 200);
    }

    @GetMapping(EndpointConstants.ADMIN)
    public ResponseEntity<List<UserDTO>> getAdmins() {
        return new ResponseEntity<>(this.userService.getUsersByRole(RoleConstants.ADMIN_ID), null, 200);
    }

    @GetMapping(EndpointConstants.OTHERS)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getUsersByRole(RoleConstants.UNKNOWN_ID), null, 200);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        return new ResponseEntity<>(this.userService.getUsersByName(name), null, 200);
    }
}
