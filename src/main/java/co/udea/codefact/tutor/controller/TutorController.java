package co.udea.codefact.tutor.controller;


import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.TUTOR)
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @Operation(summary = "Crear horario de tutor", description = "Un tutor crea su horario de disponibilidad para recibir solicitudes")
    @ApiResponse(responseCode = "200", description = "Se crea el horario satisfactoriamente" )
    @PostMapping(EndpointConstants.SCHEDULE)
    public ResponseEntity<String> createSchedule(@Valid @RequestBody TutorScheduleDTO scheduleDTO){
        this.tutorService.createTutorSchedule(scheduleDTO);
        return new ResponseEntity<>(MessagesConstants.TUTOR_SCHEDULE_CREATED, HttpStatus.OK);
    }

    @Operation(summary = "Obtener los horarios de tutor", description = "Un tutor obtiene sus horarios registrados")
    @ApiResponse(responseCode = "200", description = "Los horarios se obtuvieron correctamente")
    @GetMapping(EndpointConstants.SCHEDULE)
    public ResponseEntity<List<TutorScheduleDTO>> getTutorSchedule(){
        return new ResponseEntity<>(this.tutorService.getTutorSchedules(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener los horarios de tutor", description = "Un usuario obtiene los horarios registrados por el tutor seleccionado")
    @ApiResponse(responseCode = "200", description = "Los horarios se obtuvieron correctamente")
    @GetMapping(EndpointConstants.SCHEDULE+"/{username}")
    public ResponseEntity<List<TutorScheduleDTO>> getTutorSchedule(@PathVariable String username){
        return new ResponseEntity<>(this.tutorService.getTutorSchedules(username), HttpStatus.OK);
    }
}
