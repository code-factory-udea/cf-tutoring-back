package co.udea.codefact.subject.controller;

import java.util.List;

import co.udea.codefact.subject.service.SubjectService;
import co.udea.codefact.subject.dto.SubjectListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Materias", description = "Endpoints para info sobre materias puede ser accedidos por usuarios logueados")
@RestController
public class SubjectController {
    
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Obtener materias por código de programa", description = "Obtener todas las materias de un programa académico")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron las materias")
    @GetMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<List<SubjectListDTO>> getSubjects(@RequestParam Long academicProgramId) {
        return new ResponseEntity<>(this.subjectService.getSubjects(academicProgramId), HttpStatus.OK);
    }



}
