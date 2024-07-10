package co.udea.codefact.login;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    
    void login(LoginRequest loginRequest);

}
