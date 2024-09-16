package co.udea.codefact.administration.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import co.udea.codefact.administration.service.AdministrationService;
import co.udea.codefact.administration.dto.AssignSubjectDTO;
import co.udea.codefact.administration.dto.UnassignSubjectDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.academic.dto.AcademicProgramDTO;
import co.udea.codefact.academic.dto.FacultyDTO;
import co.udea.codefact.appointment.dto.AppointmentAllDataDTO;
import co.udea.codefact.appointment.dto.AppointmentDTO;
import co.udea.codefact.professor.dto.ProfessorDTO;
import co.udea.codefact.subject.dto.SubjectRequestDTO;
import co.udea.codefact.subject.dto.SubjectResponseDTO;
import co.udea.codefact.subject.dto.SubjectUpdateDTO;
import co.udea.codefact.tutor.dto.TutorDTO;
import co.udea.codefact.user.dto.UserChangeRoleDTO;
import co.udea.codefact.user.dto.UserDTO;
import co.udea.codefact.user.dto.UserRoleDTO;
import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.constants.RoleConstants;
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
    @PatchMapping(EndpointConstants.USER+EndpointConstants.USER_ROLE)
    public ResponseEntity<UserDTO> changeRole(@RequestBody UserChangeRoleDTO userChangeRoleDTO) {
        return new ResponseEntity<>(this.adminService.changeUserRole(userChangeRoleDTO), null, 200);
    }

    
    @Operation(summary = "Obtener usuarios estudiantes", description = "Obtener todos los usuarios con rol de estudiante")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los estudiantes")
    @GetMapping(EndpointConstants.STUDENT)
    public ResponseEntity<List<UserDTO>> getStudents() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.STUDENT_ID), null, 200);
    }

    @Operation(summary = "Obtener usuarios monitores", description = "Obtener todos los usuarios con rol de monitor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los monitores")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<UserDTO>> getTutors() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.TUTOR_ID), null, 200);
    }

    @Operation(summary = "Asignar materia a tutor", description = "Se le asigna una materia a un tutor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @ApiResponse(responseCode = "400", description = "Error al asignar la materia al tutor")
    @PostMapping(EndpointConstants.TUTOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> assignSubjectToTutor(@RequestBody AssignSubjectDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToTutor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }

    @Operation(summary = "Desasignar materia a tutor", description = "Se le elimina la materia a un tutor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @ApiResponse(responseCode = "400", description = "Error al asignar la materia al tutor")
    @PatchMapping(EndpointConstants.TUTOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> unassignSubjectToTutor(@RequestBody UnassignSubjectDTO data) {
        this.adminService.unassignSubjectToTutor(data.getUsername());
        return new ResponseEntity<>(MessagesConstants.RESPONSE_UNASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }

    @Operation(summary = "Obtener usuarios profesores", description = "Obtener todos los usuarios con rol de profesor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los profesores")
    @GetMapping(EndpointConstants.PROFESSOR)
    public ResponseEntity<List<UserDTO>> getProfessors() {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.PROFESSOR_ID), null, 200);
    }
    
    @Operation(summary = "Asignar materia a un profesor", description = "Se le asigna una materia a un profesor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @PostMapping(EndpointConstants.PROFESSOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> assignSubjectToProfessor(@RequestBody AssignSubjectDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToProfessor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR, null, 200);
    }

    @Operation(summary = "Obtener usuarios administradores", description = "Obtener todos los usuarios con rol de administrador")
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
    @GetMapping(EndpointConstants.USER)
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        return new ResponseEntity<>(this.adminService.getUsersByName(name), null, 200);
    }

    @Operation(summary = "Crear materia", description = "Crea una materia")
    @ApiResponse(responseCode = "200", description = "Materia creada satisfactoriamente")
    @PostMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<SubjectResponseDTO> createSubject(@RequestBody SubjectRequestDTO subjectDTO) {
        return new ResponseEntity<>(this.adminService.createSubject(subjectDTO), null, 200);
    }

    @Operation(summary = "Obtener roles", description = "Obtiene todos los roles disponibles")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los roles")
    @GetMapping(EndpointConstants.ROLE)
    public ResponseEntity<List<UserRoleDTO>> getRoles() {
        return new ResponseEntity<>(this.adminService.getRoles(), null, 200);
    }

    @Operation(summary = "Obtener monitorias en csv", description = "Se obtiene la información de las monitorias en formato csv")
    @ApiResponse(responseCode = "200", description = "Se genero el csv con las informaciones de las monitorias")
    @GetMapping(EndpointConstants.APPOINTMENT+EndpointConstants.CSV)
    public ResponseEntity<byte[]> getAppointmentsCSV() {
        String csv = this.adminService.appointmentsListToCSVFile();
        byte[] output = csv.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monitorias.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
        return new ResponseEntity<>(output, headers, HttpStatus.OK);
    }

    @Operation(summary = "Obtener monitorias", description = "Obtiene todas las monitorias")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron las monitorias")
    @GetMapping(EndpointConstants.APPOINTMENT)
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        return new ResponseEntity<>(this.adminService.getAllAppointments(), null, 200);
    }

    @Operation(summary = "Obtener toda la info de una monitoria", description = "Se obtiene la información de la monitoria y su calificación")
    @ApiResponse(responseCode = "200", description = "Se obtuvo la información de la monitoria")
    @GetMapping(EndpointConstants.APPOINTMENT+"/{appointmentId}")
    public ResponseEntity<AppointmentAllDataDTO> getAppointmentById(@PathVariable Long appointmentId) {
        return new ResponseEntity<>(this.adminService.getAppointmentByIdAsAdmin(appointmentId), null, 200);
    }
    
    @Operation(summary = "Modificar info de materia", description = "Modificar la información de una materia")
    @ApiResponse(responseCode = "200", description = "Se modificó la información de la materia")
    @PatchMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<SubjectResponseDTO> updateSubject(@RequestBody SubjectUpdateDTO subjectDTO) {
        return new ResponseEntity<>(this.adminService.updateSubject(subjectDTO), null, 200);
    }

    @Operation(summary = "Ver info de tutor", description = "Ver la información de un tutor")
    @GetMapping(EndpointConstants.TUTOR+"/{username}")
    public ResponseEntity<TutorDTO> getTutorInfo(@PathVariable String username){
        return new ResponseEntity<>(this.adminService.getTutorInfo(username), HttpStatus.OK);
    }

    @Operation(summary = "Ver info de profesor", description = "Ver la información de un profesor")
    @GetMapping(EndpointConstants.PROFESSOR+"/{username}")
    public ResponseEntity<ProfessorDTO> getProfessorInfo(@PathVariable String username){
        return new ResponseEntity<>(this.adminService.getProfessorInfo(username), HttpStatus.OK);
    }

    @Operation(summary = "Eliminar materia de profesor", description = "Eliminar la materia asignada a un profesor")
    @ApiResponse(responseCode = "200", description = "Se eliminó la materia del profesor")
    @DeleteMapping(EndpointConstants.PROFESSOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> deleteProfessorSubject(@RequestBody Long idProfessor) {
        this.adminService.deleteProfessorSubject(idProfessor);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR_DELETE, null, 200);
    }

    @Operation(summary = "Crear programa académico", description = "Crea un programa académico")
    @ApiResponse(responseCode = "200", description = "Programa académico creado satisfactoriamente")
    @PostMapping(EndpointConstants.ACADEMIC_PROGRAM)
    public ResponseEntity<String> createAcademicProgram(@RequestBody AcademicProgramDTO academicProgramDTO) {
        this.adminService.createAcademicProgram(academicProgramDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ACADEMIC_PROGRAM_CREATED, null, 200);
    }

    @Operation(summary = "Crear facultad", description = "Crea una facultad")
    @ApiResponse(responseCode = "200", description = "Facultad creada satisfactoriamente")
    @PostMapping(EndpointConstants.FACULTY)
    public ResponseEntity<String> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        this.adminService.createFaculty(facultyDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_FACULTY_CREATED, null, 200);
    }
}
