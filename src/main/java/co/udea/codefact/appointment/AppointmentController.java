package co.udea.codefact.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Monitorias", description = "Endpoints para la creación de Monitorias")
@RestController
@RequestMapping(EndpointConstants.APPOINTMENT)
public class AppointmentController {
    
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Crear Monitoria", description = "Crear una monitoria por parte de un usuario estudiante o monitor")
    @ApiResponse(responseCode = "200", description = "Monitoria creada")
    @PostMapping(EndpointConstants.BASE)
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        return new ResponseEntity<>(this.appointmentService.createAppointment(appointmentCreationDTO),null,200);
    }

}
