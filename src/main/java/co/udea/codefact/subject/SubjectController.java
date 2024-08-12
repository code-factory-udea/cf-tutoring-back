package co.udea.codefact.subject;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.udea.codefact.utils.constants.EndpointConstants;
@RestController
public class SubjectController {
    
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(EndpointConstants.SUBJECT)
    public ResponseEntity<List<SubjectListDTO>> getSubjects() {
        return new ResponseEntity<>(this.subjectService.getSubjects(), HttpStatus.OK);
    }
    

}
