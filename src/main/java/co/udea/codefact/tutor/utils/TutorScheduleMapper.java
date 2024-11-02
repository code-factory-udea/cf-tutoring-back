package co.udea.codefact.tutor.utils;

import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.TutorSchedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TutorScheduleMapper {

    public static List<TutorScheduleDTO> toListDTO(List<TutorSchedule> tutorSchedules) {
        List<TutorScheduleDTO> tutorScheduleDTOs = new ArrayList<>();
        for (TutorSchedule tutorSchedule : tutorSchedules) {
            tutorScheduleDTOs.add(TutorScheduleDTO.builder()
                            .id(tutorSchedule.getId())
                            .day(tutorSchedule.getDay().toString())
                            .startTime(tutorSchedule.getStartTime())
                            .endTime(tutorSchedule.getEndTime())
                            .build());
        }
        return tutorScheduleDTOs;
    }

    public static List<TutorScheduleDTO> toSlotListDTO(List<TutorSchedule> tutorSchedules) {
        List<TutorScheduleDTO> tutorScheduleDTOs = new ArrayList<>();
        for (TutorSchedule tutorSchedule : tutorSchedules) {
            LocalTime current = tutorSchedule.getStartTime();
            while(current.isBefore(tutorSchedule.getEndTime())) {
                LocalTime next = current.plusHours(1);
                if (next.isAfter(tutorSchedule.getEndTime())){
                    next = tutorSchedule.getEndTime();
                }
                tutorScheduleDTOs.add(TutorScheduleDTO.builder()
                        .id(tutorSchedule.getId())
                        .day(tutorSchedule.getDay().toString())
                        .startTime(current)
                        .endTime(next)
                        .build());
                current = next;
            }
        }
        return tutorScheduleDTOs;
    }

    private TutorScheduleMapper(){

    }

}
