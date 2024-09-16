package co.udea.codefact.login.service;

import co.udea.codefact.login.dto.LoginRequest;
import co.udea.codefact.login.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    
    LoginResponse login(LoginRequest loginRequest);

}
