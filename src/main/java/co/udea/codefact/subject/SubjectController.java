package co.udea.codefact.subject;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "Obtener materias", description = "Obtener todas las materias")
    @ApiResponse(responseCode = "200", description = "Se obtuvieron las materias")
    @GetMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<List<SubjectListDTO>> getSubjects() {
        return new ResponseEntity<>(this.subjectService.getSubjects(), HttpStatus.OK);
    }

}
