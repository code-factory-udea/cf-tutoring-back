package co.udea.codefact.academic.controller;

import java.util.List;

import co.udea.codefact.academic.dto.AcademicProgramResponseDTO;
import co.udea.codefact.academic.service.AcademicService;
import co.udea.codefact.academic.dto.FacultyResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(EndpointConstants.ACADEMIC)
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    @Operation(summary = "Obtener programas académicos", description = "Obtener todos los programas académicos")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron los programas académicos")
    @GetMapping(EndpointConstants.ACADEMIC_PROGRAM)
    public ResponseEntity<List<AcademicProgramResponseDTO>> getAcademicPrograms() {
        return new ResponseEntity<>(this.academicService.getAcademicPrograms(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener facultades", description = "Obtener todas las facultades")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron las facultades")
    @GetMapping(EndpointConstants.FACULTY)
    public ResponseEntity<List<FacultyResponseDTO>> getFaculties() {
        return new ResponseEntity<>(this.academicService.getFaculties(), HttpStatus.OK);
    }
    
}
