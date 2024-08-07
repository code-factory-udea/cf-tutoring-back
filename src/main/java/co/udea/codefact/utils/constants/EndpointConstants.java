package co.udea.codefact.utils.constants;

public class EndpointConstants {
    
    public static final String BASE = "/";
    public static final String LOGIN = "/auth/login";
    public static final String USER = "/user";
    public static final String USER_ROLE = "/user-role";
    public static final String STUDENT = "/student";
    public static final String TUTOR = "/tutor";
    public static final String PROFESSOR = "/professor";
    public static final String ADMIN = "/admin";
    public static final String OTHERS = "/others";
    
    
    private EndpointConstants() {
        throw new IllegalStateException("Utility class");
    }
}
