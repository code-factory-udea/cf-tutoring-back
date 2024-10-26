package co.udea.codefact.tutor.service;

import java.util.List;

import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.tutor.entity.TutorSchedule;
import co.udea.codefact.tutor.repository.TutorScheduleRepository;
import co.udea.codefact.utils.auth.AuthenticationUtil;
import co.udea.codefact.utils.constants.MessagesConstants;
import co.udea.codefact.utils.exceptions.DataNotFoundException;
import co.udea.codefact.utils.exceptions.InvalidBodyException;
import co.udea.codefact.utils.exceptions.TutorErrorException;
import org.springframework.stereotype.Service;


@Service
public class TutorScheduleService {
    
    private final TutorScheduleRepository tutorScheduleRepository;


    public TutorScheduleService(TutorScheduleRepository tutorScheduleRepository) {
        this.tutorScheduleRepository = tutorScheduleRepository;
    }

    public void createTutorSchedule(TutorScheduleDTO scheduleDTO, Tutor tutor) {
        if (!scheduleDTO.getStartTime().isBefore(scheduleDTO.getEndTime())) {
            throw new InvalidBodyException("La hora de inicio no puede ser mayor a la de finalización");
        }
        if (this.tutorScheduleRepository.existsOverlappingSchedule(
                tutor.getId(),
                scheduleDTO.getDay().toString(),
                scheduleDTO.getStartTime(),
                scheduleDTO.getEndTime())) {
            throw new TutorErrorException("Ya tienes un horario registrado en esta franja");
        }
        TutorSchedule tutorSchedule = TutorSchedule.builder()
                .tutor(tutor)
                .day((scheduleDTO.getDay()))
                .startTime(scheduleDTO.getStartTime())
                .endTime(scheduleDTO.getEndTime())
                .build();
        this.tutorScheduleRepository.save(tutorSchedule);
    }

    public void deleteTutorSchedule(Long id, Tutor tutor){
        TutorSchedule tutorSchedule = this.tutorScheduleRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException(MessagesConstants.TUTOR_SCHEDULE_NOT_FOUND));
        if (!tutorSchedule.getTutor().equals(tutor)){
            throw new DataNotFoundException(MessagesConstants.NO_PERMISSION);
        }
        this.tutorScheduleRepository.delete(tutorSchedule);
    }

    public void deleteTutorSchedules(Tutor tutor) {
        List<TutorSchedule> schedules = this.tutorScheduleRepository.findByTutorId(tutor.getId());
        this.tutorScheduleRepository.deleteAll(schedules);
    }

    public List<TutorSchedule> getTutorSchedules(Tutor tutor) {
        return this.tutorScheduleRepository.findByTutor(tutor);
    }

}
