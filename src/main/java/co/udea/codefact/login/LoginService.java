package co.udea.codefact.login;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    
    LoginResponse login(LoginRequest loginRequest);

}
