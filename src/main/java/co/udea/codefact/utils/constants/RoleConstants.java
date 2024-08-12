package co.udea.codefact.utils.constants;

public class RoleConstants {
    
    public static final String STUDENT = "Estudiante";
    public static final Long STUDENT_ID = 1L;
    public static final String PROFESSOR = "Profesor";
    public static final Long PROFESSOR_ID = 2L;
    public static final String UNKNOWN = "No identificado";
    public static final Long UNKNOWN_ID = 3L;
    public static final String ADMIN = "Administrador";
    public static final Long ADMIN_ID = 4L;
    public static final String TUTOR = "Monitor";
    public static final Long TUTOR_ID = 5L;

    private RoleConstants() {
        throw new IllegalStateException("Utility class");
    }
    
}
