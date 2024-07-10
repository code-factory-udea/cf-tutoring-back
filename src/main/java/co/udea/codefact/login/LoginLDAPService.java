package co.udea.codefact.login;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LoginLDAPService implements LoginService{
    
    private final WebClient webClient;

    public LoginLDAPService(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void login(LoginRequest loginRequest) {
        System.out.println("LoginLDAPService.login()");
        
        String url = "https://sistemas.udea.edu.co/api/ldap/login/";

        webClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .bodyValue(loginRequest)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println);
    }
}
