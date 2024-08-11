package co.udea.codefact.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.user.UserChangeRoleDTO;
import co.udea.codefact.user.UserDTO;
import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
import co.udea.codefact.utils.exceptions.InvalidRoleChangeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(EndpointConstants.ADMIN)
@Tag(name = "Adminstración", description = "Endpoints válidos para usuarios con rol de administrador")
public class AdministrationController {

    private final AdministrationService adminService;

    public AdministrationController(AdministrationService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Cambiar el rol de un usuario", description = "Se puede cambiar el rol de un usuario, no todos los casos son permitidos")
    @ApiResponse(responseCode = "200", description = "Cambio de rol exitoso")
    @ApiResponse(responseCode = "400", description = "Cambio de rol no permitido")
    @PatchMapping(EndpointConstants.USER_ROLE)
    public ResponseEntity<UserDTO> changeRole(@RequestBody UserChangeRoleDTO userChangeRoleDTO) {
        return new ResponseEntity<>(this.adminService.changeUserRole(userChangeRoleDTO), null, 200);
    }

    
    @Operation(summary = "Obtener estudiantes", description = "Obtener todos los usuarios con rol de estudiante")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los estudiantes")
    @GetMapping(EndpointConstants.STUDENT)
    public ResponseEntity<List<UserDTO>> getStudents() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.STUDENT_ID), null, 200);
    }

    @Operation(summary = "Obtener monitores", description = "Obtener todos los usuarios con rol de monitor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los monitores")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<UserDTO>> getTutors() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.TUTOR_ID), null, 200);
    }

    @Operation(summary = "Asignar materia a tutor", description = "Se le asigna una materia a un tutor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @ApiResponse(responseCode = "400", description = "Error al asignar la materia al tutor")
    @PostMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> assignSubjectToTutor(@RequestBody AssignSubjectDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToTutor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }

    @Operation(summary = "Obtener profesores", description = "Obtener todos los usuarios con rol de profesor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los profesores")
    @GetMapping(EndpointConstants.PROFESSOR)
    public ResponseEntity<List<UserDTO>> getProfessors() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.PROFESSOR_ID), null, 200);
    }
    
    // -------------------------------------------------------------------------
    @Operation(summary = "Asignar materia a un profesor", description = "Se le asigna una materia a un profesor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @PostMapping(EndpointConstants.PROFESSOR)
    public ResponseEntity<String> assignSubjectToProfessor(@RequestBody AssignSubjectDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToProfessor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }
    // -------------------------------------------------------------------------

    @Operation(summary = "Obtener administradores", description = "Obtener todos los usuarios con rol de administrador")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los administradores")
    @GetMapping(EndpointConstants.ADMIN)
    public ResponseEntity<List<UserDTO>> getAdmins() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.ADMIN_ID), null, 200);
    }

    @Operation(summary = "Obtener usuarios no identificados", description = "Obtener todos los usuarios con rol no identificado")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los usuarios con rol no identificado")
    @GetMapping(EndpointConstants.OTHERS)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.UNKNOWN_ID), null, 200);
    }

    @Operation(summary = "Busqueda por nombre de usuario", description = "Busca todos los usuarios que contengan el nombre dado")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los usuarios que contengan el nombre dado")
    @GetMapping
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        return new ResponseEntity<>(this.adminService.getUsersByName(name), null, 200);
    }

    @ExceptionHandler(InvalidRoleChangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidRoleChangeException(InvalidRoleChangeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

}
