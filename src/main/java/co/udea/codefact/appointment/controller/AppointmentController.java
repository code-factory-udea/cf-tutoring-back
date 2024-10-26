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
    public ResponseEntity<String> tutorResponseToAppointmentRequest(AppointmentTutorResponseDTO tutorResponseDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorResponseToAppointment(tutorResponseDTO), HttpStatus.OK);
    }

    @Operation(summary = "Tutor - Obtener las solicitudes de tutoría de un monitor con base a su estado", description = "Un tutor carga las solicitudes que a recibido")
    @ApiResponse(responseCode = "200", description = "Se cargan las solicitudes")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<AppointmentTutorDTO>> tutorGetAllAppointmentRequest(AppointmentGetTutorDTO appointmentGetTutorDTO){
        return new ResponseEntity<>(this.appointmentFacade.tutorAppointmentsRequest(appointmentGetTutorDTO), HttpStatus.OK);
    }


}
