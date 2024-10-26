package co.udea.codefact.tutor.utils;

import co.udea.codefact.tutor.dto.TutorScheduleDTO;
import co.udea.codefact.tutor.entity.TutorSchedule;
import co.udea.codefact.utils.exceptions.InvalidBodyException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TutorScheduleMapper {

    public static List<TutorScheduleDTO> toListDTO(List<TutorSchedule> tutorSchedules) {
        List<TutorScheduleDTO> tutorScheduleDTOs = new ArrayList<>();
        for (TutorSchedule tutorSchedule : tutorSchedules) {
            tutorScheduleDTOs.add(TutorScheduleDTO.builder()
                            .day(tutorSchedule.getDay().toString())
                            .startTime(tutorSchedule.getStartTime())
                            .endTime(tutorSchedule.getEndTime())
                            .build());
        }
        return tutorScheduleDTOs;
    }

}
