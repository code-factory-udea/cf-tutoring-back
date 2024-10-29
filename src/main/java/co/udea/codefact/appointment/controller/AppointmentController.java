package co.udea.codefact.appointment.controller;

import co.udea.codefact.appointment.dto.*;
import co.udea.codefact.appointment.service.AppointmentFacade;
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
    @PostMapping(EndpointConstants.BASE)
    public ResponseEntity<AppointmentDTO> studentCreateAppointment(@Valid @RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        return new ResponseEntity<>(this.appointmentFacade.studentRequestAppointment(appointmentCreationDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Respuesta a solicitud de monitoría", description = "Un tutor responde a una solicitud de monitoría que recibió anteriormente")
    @ApiResponse(responseCode = "200", description = "Se envió correctamente la respuesta a la solicitud")
    @PostMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> tutorResponseToAppointmentRequest(@Valid @RequestBody AppointmentTutorResponseDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorResponseToAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Cancela una monitoría", description = "Un tutor cancela una monitoría que estaba programada")
    @ApiResponse(responseCode = "200", description = "Se cambia el estado de la monitoría y se notifica al estudiante")
    @DeleteMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> tutorCancelAppointment(@Valid @RequestBody AppointmentIDDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorCancelAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Completa una monitoría", description = "Un tutor completa una monitoría que realizó")
    @ApiResponse(responseCode = "200", description = "Se cambia el estado de la monitoría y se habilita su calificación")
    @PatchMapping(EndpointConstants.TUTOR)
    public ResponseEntity<String> tutorCompleteAppointment(@Valid @RequestBody AppointmentIDDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorCompleteAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Obtener las solicitudes de tutoría de un monitor con base a su estado", description = "Un tutor carga las solicitudes que a recibido")
    @ApiResponse(responseCode = "200", description = "Se cargan las solicitudes")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<AppointmentTutorDTO>> tutorGetAllAppointmentRequest(@Valid AppointmentGetTutorDTO appointmentGetTutorDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorAppointmentsRequest(appointmentGetTutorDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Obtener la información completa de una tutoría específica que fue completada", description = "Un tutor carga la información de una tutoría que impartió")
    @ApiResponse(responseCode = "200", description = "Se carga la información completa")
    @GetMapping(EndpointConstants.TUTOR+"/{appointmentId}")
    public ResponseEntity<AppointmentAllDataDTO> tutorGetAppointmentCompleted(@PathVariable Long appointmentId){
        return new ResponseEntity<>(this.appointmentFacade.getCompletedAppointmentInfo(appointmentId), HttpStatus.OK);
    }

}
