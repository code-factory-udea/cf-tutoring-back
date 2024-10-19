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
    private final AuthenticationUtil authenticationUtil;
    private final TutorService tutorService;


    public TutorScheduleService(TutorScheduleRepository tutorScheduleRepository,
                                TutorService tutorService,
                                AuthenticationUtil authenticationUtil) {
        this.tutorScheduleRepository = tutorScheduleRepository;
        this.tutorService = tutorService;
        this.authenticationUtil = authenticationUtil;
    }

    public void createTutorSchedule(TutorScheduleDTO scheduleDTO) {
        if (!scheduleDTO.getStartTime().isBefore(scheduleDTO.getEndTime())) {
            throw new InvalidBodyException("La hora de inicio no puede ser mayor a la de finalización");
        }
        Tutor tutor = this.tutorService.getTutorByUsername(this.authenticationUtil.getAuthenticatedUser())
                .orElseThrow(() -> new DataNotFoundException(MessagesConstants.TUTOR_WITHOUT_SUBJECT));
        System.out.println("Antes del if");
        System.out.println(this.tutorScheduleRepository.existsByTutorAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                tutor, scheduleDTO.getDay(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime()));
        if (this.tutorScheduleRepository.existsByTutorAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                tutor, scheduleDTO.getDay(), scheduleDTO.getStartTime(), scheduleDTO.getEndTime())
        ) {
            throw new TutorErrorException("Ya tienes un horario registrado en esta franja");
        }
        System.out.println("Después del if");
        TutorSchedule tutorSchedule = TutorSchedule.builder()
                .tutor(tutor)
                .day(scheduleDTO.getDay())
                .startTime(scheduleDTO.getStartTime())
                .endTime(scheduleDTO.getEndTime())
                .build();
        System.out.println(tutorSchedule);
        this.tutorScheduleRepository.save(tutorSchedule);
    }


    public void deleteTutorSchedules(Tutor tutor) {
        List<TutorSchedule> schedules = this.tutorScheduleRepository.findByTutorId(tutor.getId());
        this.tutorScheduleRepository.deleteAll(schedules);
    }

    public List<TutorSchedule> getTutorSchedules(Long tutorId) {
        return this.tutorScheduleRepository.findByTutorId(tutorId);
    }
}
