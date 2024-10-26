package co.udea.codefact.utils.constants;

public class MessagesConstants {

    public static final String INVALID_CREDENTIALS = "Usuario y/o contraseña incorrectos";
    public static final String INVALID_ROLE_CHANGE = "No es un cambio de rol válido";
    public static final String TUTOR_NOT_FOUND = "Tutor no encontrado";
    public static final String USER_NOT_FOUND = "Usuario no encontrado";
    public static final String USER_ROLE_NOT_FOUND = "Rol de usuario no encontrado";
    public static final String ACADEMIC_PROGRAM_NOT_FOUND = "Programa académico no encontrado";
    public static final String FACULTY_NOT_FOUND = "Facultad no encontrada";
    public static final String SUBJECT_NOT_FOUND = "Materia no encontrada";
    public static final String PROFESSOR_NOT_FOUND = "Profesor no encontrado";
    public static final String PROFESSOR_NOT_FOUND_OR_UNASSIGNED = "No se ha encontrado información o no tiene materia asignada";
    public static final String TUTOR_NOT_FOUND_OR_UNASSIGNED = "No se ha encontrado información o no tiene materia asignada";
    public static final String ACADEMIC_PROGRAM_ALREADY_EXISTS = "El programa académico ya existe";
    public static final String FACULTY_ALREADY_EXISTS = "La facultad ya existe";
    public static final String SUBJECT_ALREADY_EXISTS = "La materia ya existe";
    public static final String APPOINTMENT_NOT_FOUND = "Monitoría no encontrada";
    public static final String NO_DATA = "Sin asignar";
    public static final String PROFESSOR_SUBJECT_NOT_FOUND = "Materia no asignada al profesor";
    public static final String PROFESSOR_SUBJECT_ALREADY_EXISTS = "Ya tiene esta materia asignada";
    public static final String TUTOR_SCHEDULE_NOT_FOUND = "Tutor no encontrado";
    public static final String TUTOR_APPOINTMENT_FINAL_STATUS = "Esta solicitud de monitoría no está en el estado esperado";

    public static final String TUTOR_WITHOUT_SUBJECT = "Se debe asignar una materia primero";

    public static final String NO_PERMISSION = "No tiene permisos para realizar esta acción";
    public static final String RESPONSE_ASSIGN_SUBJECT_TO_TUTOR = "Materia asignada al monitor";
    public static final String RESPONSE_UNASSIGN_SUBJECT_TO_TUTOR = "Materia eliminada al monitor";
    public static final String RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR = "Materia asignada al profesor";
    public static final String RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR_DELETE = "Materia eliminada del profesor";

    public static final String RESPONSE_ACADEMIC_PROGRAM_CREATED = "Programa académico creado exitosamente";
    public static final String RESPONSE_ACADEMIC_PROGRAM_UPDATED = "Programa académico actualizado exitosamente";
    public static final String RESPONSE_FACULTY_CREATED = "Facultad creada exitosamente";

    public static final String RESPONSE_TUTOR_APPOINTMENT_ACCEPTED = "La monitoría ha sido aceptada, el estudiante será notificado";
    public static final String RESPONSE_TUTOR_APPOINTMENT_REJECTED = "La solicitud de monitoría ha sido rechazada";
    public static final String RESPONSE_TUTOR_APPOINTMENT_COMPLETED = "La monitoría se ha completado, el estudiante ahora podrá calificarla";
    public static final String RESPONSE_TUTOR_APPOINTMENT_CANCELLED = "La monitoría ha sido cancelada, se le notificará al estudiante";


    public static final String RESPONSE_TUTOR_SCHEDULE_DELETED = "El horario ha sido eliminado";

    public static final String ERROR_EXCEPTION_MESSAGE_BODY = "message";

    public static final String ERROR_PARSING_HOUR_DAY = "Ingresa un horario válido";
    public static final String ERROR_RESPONSE_APPOINTMENT_INVALID = "La respuesta a la solicitud de monitoria no es válida";

    public static final String TUTOR_SCHEDULE_CREATED = "El horario se ha creado correctamente";
    public static final String TUTOR_LINK_ASSIGNED = "El link se ha asignado correctamente";


    private MessagesConstants() {
        throw new IllegalStateException("Utility class");
    }
    
}
