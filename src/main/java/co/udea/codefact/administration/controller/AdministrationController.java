package co.udea.codefact.administration.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import co.udea.codefact.academic.dto.UpdateAcademicProgramDTO;
import co.udea.codefact.administration.dto.DeleteSubjectDTO;
import co.udea.codefact.administration.dto.GetAllAppointmentsDTO;
import co.udea.codefact.administration.dto.UserPaginationDTO;
import co.udea.codefact.administration.service.AdministrationService;
import co.udea.codefact.administration.dto.SubjectAssignmentDTO;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserDTO> changeRole(@Valid @RequestBody UserChangeRoleDTO userChangeRoleDTO) {
        return new ResponseEntity<>(this.adminService.changeUserRole(userChangeRoleDTO), null, 200);
    }

    
    @Operation(summary = "Obtener usuarios estudiantes", description = "Obtener todos los usuarios con rol de estudiante")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los estudiantes")
    @GetMapping(EndpointConstants.STUDENT)
    public ResponseEntity<UserPaginationDTO> getStudents(@RequestParam int page, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.STUDENT_ID, page, name), null, 200);
    }

    @Operation(summary = "Obtener usuarios monitores", description = "Obtener todos los usuarios con rol de monitor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los monitores")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<UserPaginationDTO>  getTutors(@RequestParam int page, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.TUTOR_ID, page, name), null, 200);
    }

    @Operation(summary = "Asignar materia a tutor", description = "Se le asigna una materia a un tutor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @ApiResponse(responseCode = "400", description = "Error al asignar la materia al tutor")
    @PostMapping(EndpointConstants.TUTOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> assignSubjectToTutor(@Valid @RequestBody SubjectAssignmentDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToTutor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }

    @Operation(summary = "Desasignar materia a tutor", description = "Se le elimina la materia a un tutor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @ApiResponse(responseCode = "400", description = "Error al asignar la materia al tutor")
    @PatchMapping(EndpointConstants.TUTOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> unassignSubjectToTutor(@Valid @RequestBody DeleteSubjectDTO dto) {
        this.adminService.unassignSubjectToTutor(dto);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_UNASSIGN_SUBJECT_TO_TUTOR, null, 200);
    }

    @Operation(summary = "Obtener usuarios profesores", description = "Obtener todos los usuarios con rol de profesor")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los profesores")
    @GetMapping(EndpointConstants.PROFESSOR)
    public ResponseEntity<UserPaginationDTO> getProfessors(@RequestParam int page, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.PROFESSOR_ID, page, name), null, 200);
    }
    
    @Operation(summary = "Asignar materia a un profesor", description = "Se le asigna una materia a un profesor")
    @ApiResponse(responseCode = "200", description = "Materia asignada satisfactoriamente")
    @PostMapping(EndpointConstants.PROFESSOR+EndpointConstants.SUBJECT)
    public ResponseEntity<String> assignSubjectToProfessor(@Valid @RequestBody SubjectAssignmentDTO tutorSubjectDTO) {
        this.adminService.assignSubjectToProfessor(tutorSubjectDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR, null, 200);
    }

    @Operation(summary = "Obtener usuarios administradores", description = "Obtener todos los usuarios con rol de administrador")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los administradores")
    @GetMapping(EndpointConstants.ADMIN)
    public ResponseEntity<UserPaginationDTO> getAdmins(@RequestParam int page, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.ADMIN_ID, page, name), null, 200);
    }

    @Operation(summary = "Obtener usuarios no identificados", description = "Obtener todos los usuarios con rol no identificado")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los usuarios con rol no identificado")
    @GetMapping(EndpointConstants.OTHERS)
    public ResponseEntity<UserPaginationDTO> getNotIdentifiedUsers(@RequestParam int page, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(this.adminService.getUsersByRole(RoleConstants.UNKNOWN_ID, page, name), null, 200);
    }

    @Operation(summary = "Crear materia", description = "Crea una materia")
    @ApiResponse(responseCode = "200", description = "Materia creada satisfactoriamente")
    @PostMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<SubjectResponseDTO> createSubject(@Valid @RequestBody SubjectRequestDTO subjectDTO) {
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
    @PostMapping(EndpointConstants.APPOINTMENT+EndpointConstants.CSV)
    public ResponseEntity<byte[]> getAppointmentsCSV(@Valid @RequestBody GetAllAppointmentsDTO getAllAppointmentsDTO) {
        String csv = this.adminService.appointmentsListToCSVFile(getAllAppointmentsDTO.getInitialDate(),
                getAllAppointmentsDTO.getFinalDate());
        byte[] output = csv.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=monitorias.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
        return new ResponseEntity<>(output, headers, HttpStatus.OK);
    }

    @Operation(summary = "Obtener monitorias", description = "Obtiene todas las monitorias")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron las monitorias")
    @GetMapping(EndpointConstants.APPOINTMENT)
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@RequestParam LocalDate initialDate, @RequestParam LocalDate finalDate) {
        return new ResponseEntity<>(this.adminService.getAllAppointments(initialDate, finalDate), null, 200);
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
    public ResponseEntity<SubjectResponseDTO> updateSubject(@Valid @RequestBody SubjectUpdateDTO subjectDTO) {
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
    public ResponseEntity<String> deleteProfessorSubject(@Valid @RequestBody DeleteSubjectDTO deleteProfessorSubjectDTO) {
        this.adminService.deleteProfessorSubject(deleteProfessorSubjectDTO.getId());
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ASSIGN_SUBJECT_TO_PROFESSOR_DELETE, null, 200);
    }

    @Operation(summary = "Crear programa académico", description = "Crea un programa académico")
    @ApiResponse(responseCode = "200", description = "Programa académico creado satisfactoriamente")
    @PostMapping(EndpointConstants.ACADEMIC_PROGRAM)
    public ResponseEntity<String> createAcademicProgram(@Valid @RequestBody AcademicProgramDTO academicProgramDTO) {
        this.adminService.createAcademicProgram(academicProgramDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ACADEMIC_PROGRAM_CREATED, null, 200);
    }

    @Operation(summary = "Crear facultad", description = "Crea una facultad")
    @ApiResponse(responseCode = "200", description = "Facultad creada satisfactoriamente")
    @PostMapping(EndpointConstants.FACULTY)
    public ResponseEntity<String> createFaculty(@Valid @RequestBody FacultyDTO facultyDTO) {
        this.adminService.createFaculty(facultyDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_FACULTY_CREATED, null, 200);
    }

    @Operation(summary = "Actualizar info de programa académico", description = "Actualiza la información de un programa académico")
    @ApiResponse(responseCode = "200", description = "Se actualiza el programa académico ")
    @PatchMapping(EndpointConstants.ACADEMIC_PROGRAM)
    public ResponseEntity<String> updateAcademicProgram(
            @Valid @RequestBody UpdateAcademicProgramDTO updateAcademicProgramDTO) {
        this.adminService.updateAcademicProgramInfo(updateAcademicProgramDTO);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_ACADEMIC_PROGRAM_UPDATED, HttpStatus.OK);
    }
}
