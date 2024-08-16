package co.udea.codefact.utils.constants;

public class EndpointConstants {
    
    public static final String BASE = "/";
    public static final String LOGIN = "/auth/login";
    public static final String USER = "/user";
    public static final String USER_ROLE = "/user-role";
    public static final String ROLE = "/role";
    public static final String STUDENT = "/student";
    public static final String TUTOR = "/tutor";
    public static final String PROFESSOR = "/professor";
    public static final String ADMIN = "/admin";
    public static final String OTHERS = "/others";
    public static final String ACADEMIC = "/academic";
    public static final String ACADEMIC_PROGRAM = "/academic-program";
    public static final String FACULTY = "/faculty";
    public static final String SUBJECT = "/subject";
    public static final String APPOINTMENT = "/appointment";
    public static final String CSV = "/csv";
    
    private EndpointConstants() {
        throw new IllegalStateException("Utility class");
    }
}
