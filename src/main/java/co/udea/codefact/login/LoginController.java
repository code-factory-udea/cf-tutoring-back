package co.udea.codefact.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginLDAPService loginService) {
        this.loginService = loginService;
    }
    
    @PostMapping("/")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        this.loginService.login(loginRequest);
        return new ResponseEntity<>("API para codefact", HttpStatus.OK);
    }
}
