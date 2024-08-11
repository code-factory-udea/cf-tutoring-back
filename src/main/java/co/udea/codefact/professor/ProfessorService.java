package co.udea.codefact.professor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.subject.Subject;
import co.udea.codefact.user.User;

@Service
public class ProfessorService {
    
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public Professor createProfessor(User user) {
        Optional<Professor> professor = this.professorRepository.findByUserId(user.getId());
        if (!professor.isPresent()) {
            return this.professorRepository.save(Professor.builder().user(user).build());
        }
        return professor.get();
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public void assignSubject(User user, Subject subject) {
        Optional<Professor> professor = this.professorRepository.findTopByUserIdOrderByIdDesc(user.getId());
        if (professor.isPresent() && !professor.get().hasSubject()) {
            professor.get().setSubject(subject);
            this.professorRepository.save(professor.get());
            return;
        }
        Professor newProfessor = Professor.builder().user(user).subject(subject).build();
        this.professorRepository.save(newProfessor);
    }
    
}
