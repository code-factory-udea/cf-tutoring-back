package co.udea.codefact.tutor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import co.udea.codefact.subject.Subject;
import co.udea.codefact.user.User;
import co.udea.codefact.user.UserService;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;

@Service
public class TutorService {
    
    private final TutorRepository tutorRepository;
    private final TutorScheduleService tutorScheduleService;
    private final UserService userService;
    

    public TutorService(TutorRepository tutorRepository, TutorScheduleService tutorScheduleService, UserService userService) {
        this.tutorRepository = tutorRepository;
        this.tutorScheduleService = tutorScheduleService;
        this.userService = userService;
    }

    public void enableTutor(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (tutor.isPresent()) {
            tutor.get().setIsActive(true);
            this.tutorRepository.save(tutor.get());
            return;
        }
        Tutor newTutor = Tutor.builder().user(user).isActive(true).build();
        this.tutorRepository.save(newTutor);
    }

    public void disableTutor(User user) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (!tutor.isPresent()) {
            throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);    
        }
        tutor.get().setIsActive(false);
        this.tutorRepository.save(tutor.get());
        this.tutorScheduleService.deleteTutorSchedules(tutor.get());
    }

    public void assignSubject(User user, Subject subject) {
        Optional<Tutor> tutor = this.tutorRepository.findByUserId(user.getId());
        if (!tutor.isPresent()) {
          throw new DataNotFoundException(MessagesConstants.TUTOR_NOT_FOUND);
        }
        tutor.get().setSubject(subject);
        this.tutorRepository.save(tutor.get());
    }


}

