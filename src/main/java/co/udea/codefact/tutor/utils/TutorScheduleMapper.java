package co.udea.codefact.tutor.utils;

import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.TutorSchedule;

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

}
