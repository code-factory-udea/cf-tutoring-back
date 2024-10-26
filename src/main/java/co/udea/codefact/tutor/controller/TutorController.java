package co.udea.codefact.tutor.controller;


import co.udea.codefact.tutor.dto.CreateTutorScheduleDTO;
import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.dto.VirtualLinkDTO;
import co.udea.codefact.tutor.service.TutorService;
import co.udea.codefact.utils.constants.EndpointConstants;
import co.udea.codefact.utils.constants.MessagesConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tutores", description = "Endpoints para la gestión de la información de usuarios tutores")
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
    public ResponseEntity<String> createSchedule(@Valid @RequestBody CreateTutorScheduleDTO scheduleDTO){
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

    @Operation(summary = "Eliminar un horario de tutor", description = "Un tutor puede eliminar uno de sus horarios registrados")
    @ApiResponse(responseCode = "200", description = "Horario eliminado exitosamente")
    @DeleteMapping(EndpointConstants.SCHEDULE+"/{scheduleId}")
    public ResponseEntity<String> deleteTutorSchedule(@PathVariable Long scheduleId){
        this.tutorService.deleteTutorSchedule(scheduleId);
        return new ResponseEntity<>(MessagesConstants.RESPONSE_TUTOR_SCHEDULE_DELETED, HttpStatus.OK);
    }

    @Operation(summary = "Asignar link de reuniones de tutor", description = "Un tutor asigna el link en el que atenderá a los estudiantes")
    @ApiResponse(responseCode = "200", description = "Se agrega el link de reuniones al tutor" )
    @PostMapping("/virtual")
    public ResponseEntity<String> assignVirtualLink(@Valid @RequestBody VirtualLinkDTO virtualLinkDTO){
        this.tutorService.assignVirtualLink(virtualLinkDTO);
        return new ResponseEntity<>(MessagesConstants.TUTOR_LINK_ASSIGNED, HttpStatus.OK);
    }

    @Operation(summary = "Obtener link de reuniones de tutor", description = "Obtiene el link en el que atenderá a los estudiantes")
    @ApiResponse(responseCode = "200", description = "Se obtiene el link de reuniones del tutor" )
    @GetMapping("/virtual")
    public ResponseEntity<VirtualLinkDTO> getVirtualLink(){
        return new ResponseEntity<>(this.tutorService.getVirtualLink(), HttpStatus.OK);
    }

}
