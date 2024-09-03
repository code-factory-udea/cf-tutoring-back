package co.udea.codefact.tutor.service;

import java.util.List;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import co.udea.codefact.tutor.repository.TutorScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class TutorScheduleService {
    
    private final TutorScheduleRepository tutorScheduleRepository;

    public TutorScheduleService(TutorScheduleRepository tutorScheduleRepository) {
        this.tutorScheduleRepository = tutorScheduleRepository;
    }

    public void deleteTutorSchedules(Tutor tutor) {
        List<TutorSchedule> schedules = this.tutorScheduleRepository.findByTutorId(tutor.getId());
        for (TutorSchedule schedule : schedules) {
            this.tutorScheduleRepository.delete(schedule);
        }
    }

    public List<TutorSchedule> getTutorSchedules(Long tutorId) {
        return this.tutorScheduleRepository.findByTutorId(tutorId);
    }
}
