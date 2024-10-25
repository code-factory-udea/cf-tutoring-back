package co.udea.codefact.utils.constants;

public class ConstraintsConstants {

    public static final String USERNAME_NOT_NULL = "El nombre de usuario no puede ser nulo";
    public static final String USERNAME_SIZE = "El nombre de usuario debe ser de 1 a 50 caracteres";

    public static final String SUBJECT_CODE_NOT_NULL = "El código de la materia no puede ser nulo";
    public static final String SUBJECT_NAME_NOT_BLANK = "El nombre de la materia no puede estar vacío";
    public static final String SUBJECT_NAME_NOT_NULL = "El nombre de la materia no puede ser nulo";

    public static final String ACADEMIC_PROGRAM_CODE_NOT_NULL = "El código del pograma académico no puede ser nulo";
    public static final String FACULTY_CODE_NOT_NULL = "El código de la facultad no puede ser nulo";

    public static final String USER_ROLE_NOT_NULL = "El rol del usuario no puede ser nulo";

    public static final String ID_NOT_NULL = "El ID no puede ser nulo";
    public static final String APPOINTMENT_RESPONSE_NOT_NULL = "La respuesta a la solicitud de monitoría no puede ser nula";

    private ConstraintsConstants() { throw new IllegalStateException("Utility class"); }
}
