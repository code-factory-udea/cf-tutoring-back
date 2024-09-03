package co.udea.codefact.login.service;

import co.udea.codefact.login.dto.LoginLDAPResponse;
import co.udea.codefact.login.dto.LoginRequest;
import co.udea.codefact.login.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import co.udea.codefact.config.jwt.JWTService;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.user.utils.UserMapper;
import co.udea.codefact.user.service.UserService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.URLConstants;
import co.udea.codefact.utils.exceptions.InvalidCredentialsException;
import reactor.core.publisher.Mono;

@Service
public class LoginLDAPService implements LoginService{
    
    private final WebClient webClient;
    private final JWTService jwtService;
    private final UserService userService;

    public LoginLDAPService(WebClient webClient, JWTService jwtService, UserService userService) {
        this.webClient = webClient;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        @SuppressWarnings("unlikely-arg-type")
        Mono<User> dto = webClient.post()
                    .uri(URLConstants.URL_LDAP)
                    .header("Content-Type", "application/json")
                    .bodyValue(loginRequest)
                    .retrieve()
                    .onStatus(HttpStatus.UNAUTHORIZED::equals, clientResponse -> Mono.error(new InvalidCredentialsException(MessagesConstants.INVALID_CREDENTIALS)))
                    .bodyToMono(LoginLDAPResponse.class)
                    .flatMap(this::handleResponse);
            
        String token = jwtService.generateToken(dto.block());
        
        return LoginResponse.builder()
            .user(UserMapper.toLoginUserDTO(dto.block()))
            .token(token)
            .build();
        }

        private Mono<User> handleResponse(LoginLDAPResponse loginLDAPResponse) {
            return Mono.just(this.userService.loginUser(loginLDAPResponse));
        }

        

        
}
