package co.udea.codefact.tutor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.user.User;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.TutorNotFoundException;

@Service
public class TutorService {
    
    private final TutorRepository tutorRepository;
    private final TutorScheduleService tutorScheduleService;

    public TutorService(TutorRepository tutorRepository, TutorScheduleService tutorScheduleService) {
        this.tutorRepository = tutorRepository;
        this.tutorScheduleService = tutorScheduleService;
    }

    public void enableTutor(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (tutor.isPresent()) {
            tutor.get().setIsActive(true);
            this.tutorRepository.save(tutor.get());
            return;
        }
        System.out.println("Pasé el if");
        Tutor newTutor = Tutor.builder().user(user).isActive(true).build();
        System.out.println(newTutor);
        this.tutorRepository.save(newTutor);
    }

    public void disableTutor(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (!tutor.isPresent()) {
            throw new TutorNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);    
        }
        tutor.get().setIsActive(false);
        this.tutorRepository.save(tutor.get());
        this.tutorScheduleService.deleteTutorSchedules(tutor.get());
    }
}
