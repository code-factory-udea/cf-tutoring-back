package co.udea.codefact.professor;

import java.util.Optional;

import org.springframework.stereotype.Service;

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
    
}
