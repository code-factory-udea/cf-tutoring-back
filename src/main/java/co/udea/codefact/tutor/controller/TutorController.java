package co.udea.codefact.tutor.controller;


import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.service.TutorScheduleService;
import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EndpointConstants.TUTOR)
public class TutorController {

    private final TutorScheduleService tutorScheduleService;

    public TutorController(TutorScheduleService tutorScheduleService) {
        this.tutorScheduleService = tutorScheduleService;
    }

    @Operation(summary = "Crear horario de tutor", description = "Un tutor crea su horario de disponibilidad para recibir solicitudes")
    @ApiResponse(responseCode = "200", description = "Se crea el horario satisfactoriamente" )
    @PostMapping(EndpointConstants.SCHEDULE)
    public ResponseEntity<String> createSchedule(@Valid @RequestBody TutorScheduleDTO scheduleDTO){
        this.tutorScheduleService.createTutorSchedule(scheduleDTO);
        return new ResponseEntity<>(MessagesConstants.TUTOR_SCHEDULE_CREATED, HttpStatus.OK);
    }



}
