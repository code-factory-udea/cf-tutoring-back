package co.udea.codefact.professor.controller;

import co.udea.codefact.professor.service.ProfessorService;
import co.udea.codefact.subject.dto.SubjectListDTO;
import co.udea.codefact.tutor.dto.TutorListDTO;
import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Profesores", description = "Endpoints para el perfil de profesor")
@RestController
@RequestMapping(EndpointConstants.PROFESSOR)
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @Operation(summary = "Obtener las materias asignadas a un profesor", description = "Un profesor obtiene las materias asignadas así mismo")
    @ApiResponse(responseCode = "200", description = "Lista de materias cargadas")
    @GetMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<List<SubjectListDTO>> getProfessorSubject(){
        return new ResponseEntity<>(this.professorService.getProfessorSubject(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener los tutores asociados a una materia asignada al profesor", description = "Un profesor obtiene los tutores asociados a una materia que tiene asignada")
    @ApiResponse(responseCode = "200", description = "Lista de tutores asociados a una materia seleccionada")
    @GetMapping(EndpointConstants.TUTOR)
    public ResponseEntity<List<TutorListDTO>> getTutorsProfessorSubject(Long subjectCode){
        return new ResponseEntity<>(this.professorService.getTutorsBySubject(subjectCode), HttpStatus.OK);
    }

    @Operation(summary = "Obtener las monitorias completadas de un tutor", description = "Un profesor obtiene las monitorias completadas de un tutor")
    @ApiResponse(responseCode = "200", description = "Lista de monitorias completadas por un tutor especifico")
    @GetMapping(EndpointConstants.APPOINTMENT)
    public ResponseEntity<?> getTutorsProfessorAppointments(@RequestParam String username,
                                                            @RequestParam LocalDate initialDate,
                                                            @RequestParam LocalDate finalDate){
        return new ResponseEntity<>(this.professorService.getTutorsCompletedAppointments(username,
                initialDate, finalDate), HttpStatus.OK);
    }

}
