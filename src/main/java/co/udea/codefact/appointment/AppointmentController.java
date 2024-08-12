package co.udea.codefact.appointment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;

@RestController
@RequestMapping(EndpointConstants.APPOINTMENT)
public class AppointmentController {
    
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping(EndpointConstants.BASE)
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentCreationDTO appointmentCreationDTO) {
        return new ResponseEntity<>(this.appointmentService.createAppointment(appointmentCreationDTO),null,200);
    }

}
