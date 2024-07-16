package co.udea.codefact.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginLDAPResponse {
    
    @JsonProperty("user")
    private String user;
    @JsonProperty("role")
    private String role;
    @JsonProperty("name")
    private String name;
    @JsonProperty("lastName")
    private String lastName;
}
