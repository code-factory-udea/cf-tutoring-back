package co.udea.codefact.login;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    
    @GetMapping("/")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>("API para codefact", HttpStatus.OK);
    }
}
