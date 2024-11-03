package co.udea.codefact.appointment.controller;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.service.AppointmentFacade;
import co.udea.codefact.tutor.dto.TutorListDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Monitorias", description = "Endpoints para la creación de Monitorias")
@RestController
@RequestMapping(EndpointConstants.APPOINTMENT)
public class AppointmentController {
    
    private final AppointmentFacade appointmentFacade;

    public AppointmentController(AppointmentFacade appointmentFacade) {
        this.appointmentFacade = appointmentFacade;
    }

    @Operation(summary = "Estudiante - Crear solicitud de monitoria", description = "Crear una monitoria por parte de un usuario estudiante o monitor")
    @ApiResponse(responseCode = "200", description = "Monitoria creada")
    @PostMapping(EndpointConstants.STUDENT)
    public ResponseEntity<AppointmentDTO> studentCreateAppointment(@Valid @RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        return new ResponseEntity<>(this.appointmentFacade.studentRequestAppointment(appointmentCreationDTO), HttpStatus.OK);
    }

    @Operation(summary = "Estudiante - Busca los monitores asociados a una materia", description = "Manda el id de una materia para saber que tutores están asignados a ellas")
    @ApiResponse(responseCode = "200", description = "Se carga un listado de tutores")
    @GetMapping(EndpointConstants.STUDENT+"/{subjectId}")
    public ResponseEntity<List<TutorListDTO>> studentGetTutorsBySubject(@PathVariable Long subjectId) {
        return new ResponseEntity<>(this.appointmentFacade.studentGetTutorsBySubject(subjectId), HttpStatus.OK);
    }

    @Operation(summary = "Estudiante - Busca los horarios de un tutor", description = "Carga los horarios registrados por un tutor")
    @ApiResponse(responseCode = "200", description = "Carga los horarios de un tutor")
    @GetMapping(EndpointConstants.STUDENT+EndpointConstants.TUTOR+"/{username}")
    public ResponseEntity<List<?>> studentGetTutorsSchedule(@PathVariable String username) {
        return new ResponseEntity<>(this.appointmentFacade.studentGetTutorsSchedule(username), HttpStatus.OK);
    }


    @Operation(summary = "Estudiante - Calificar una monitoría", description = "Un estudiante califica una monitoria recibida")
    @ApiResponse(responseCode = "200", description = "Se califica la monitoria")
    @PostMapping(EndpointConstants.STUDENT+EndpointConstants.SURVEY)
    public ResponseEntity<String> studentGetTutorsBySubject(@Valid @RequestBody SatisfactionSurveyDTO satisfactionSurveyDTO)  {
        return new ResponseEntity<>(this.appointmentFacade.studentSatisfactionSurveyAppointment(satisfactionSurveyDTO), HttpStatus.OK);
    }

    @Operation(summary = "Estudiante - Obtener las solicitudes de tutoría de un monitor con base a su estado", description = "Un estudiante carga las solicitudes que ha enviado")
    @ApiResponse(responseCode = "200", description = "Se cargan las solicitudes")
    @GetMapping(EndpointConstants.STUDENT)
    public ResponseEntity<List<AppointmentInfoDTO>> studentGetAllAppointmentRequest(@Valid AppointmentGetInfoDTO appointmentGetInfoDTO){
        return new ResponseEntity<>(this.appointmentFacade.studentAppointmentsRequest(appointmentGetInfoDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Respuesta a solicitud de monitoría", description = "Un tutor responde a una solicitud de monitoría que recibió anteriormente")
    @ApiResponse(responseCode = "200", description = "Se envió correctamente la respuesta a la solicitud")
    @PostMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> tutorResponseToAppointmentRequest(@Valid @RequestBody AppointmentTutorResponseDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorResponseToAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Completa una monitoría", description = "Un tutor completa una monitoría que realizó")
    @ApiResponse(responseCode = "200", description = "Se cambia el estado de la monitoría y se habilita su calificación")
    @PatchMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> tutorCompleteAppointment(@Valid @RequestBody AppointmentIDDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorCompleteAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Obtener las solicitudes de tutoría de un monitor con base a su estado", description = "Un tutor carga las solicitudes que ha recibido")
    @ApiResponse(responseCode = "200", description = "Se cargan las solicitudes")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<AppointmentInfoDTO>> tutorGetAllAppointmentRequest(@Valid AppointmentGetInfoDTO appointmentGetTutorDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorAppointmentsRequest(appointmentGetTutorDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Obtener la información completa de una tutoría específica que fue completada", description = "Un tutor carga la información de una tutoría que impartió")
    @ApiResponse(responseCode = "200", description = "Se carga la información completa")
    @GetMapping(EndpointConstants.TUTOR+"/{appointmentId}")
    public ResponseEntity<AppointmentAllDataDTO> tutorGetAppointmentCompleted(@PathVariable Long appointmentId){
        return new ResponseEntity<>(this.appointmentFacade.getCompletedAppointmentInfo(appointmentId), HttpStatus.OK);
    }

}
