package co.udea.codefact.login.controller;

import co.udea.codefact.login.dto.LoginRequest;
import co.udea.codefact.login.dto.LoginResponse;
import co.udea.codefact.login.service.LoginLDAPService;
import co.udea.codefact.login.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Inicio de sesión", description = "Endpoints válidos para iniciar sesión en la aplicación")
@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginLDAPService loginService) {
        this.loginService = loginService;
    }
    
    @Operation(summary = "Iniciar sesión", description = "Iniciar sesión en la aplicación")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso")
    @PostMapping(EndpointConstants.LOGIN)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(this.loginService.login(loginRequest), HttpStatus.OK);
    }
}
