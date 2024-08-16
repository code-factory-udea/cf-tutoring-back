package co.udea.codefact.user;

import org.springframework.stereotype.Component;

import co.udea.codefact.professor.ProfessorService;
import co.udea.codefact.tutor.TutorService;
import co.udea.codefact.utils.constants.RoleConstants;
import jakarta.persistence.PostPersist;

@Component
public class UserListener {
    
    private final TutorService tutorService;
    private final ProfessorService professorService;

    public UserListener(TutorService tutorService, ProfessorService professorService) {
        this.tutorService = tutorService;
        this.professorService = professorService;
    }

    @PostPersist
    public void createTutorOrProfessor(User user) {
        if (user.getRole().getRole().equals(RoleConstants.TUTOR)) {
            this.tutorService.enableTutor(user);
        } else if (user.getRole().getRole().equals(RoleConstants.PROFESSOR)) {
            this.professorService.createProfessor(user);
        }
    }
}
