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

    private static final Map<DayOfWeek, String> dayEnumToString = new EnumMap<>(DayOfWeek.class);

    static {
        dayEnumToString.put(DayOfWeek.MONDAY, "Lunes");
        dayEnumToString.put(DayOfWeek.TUESDAY, "Martes");
        dayEnumToString.put(DayOfWeek.WEDNESDAY, "Miércoles");
        dayEnumToString.put(DayOfWeek.THURSDAY, "Jueves");
        dayEnumToString.put(DayOfWeek.FRIDAY, "Viernes");
        dayEnumToString.put(DayOfWeek.SATURDAY, "Sábado");
        dayEnumToString.put(DayOfWeek.SUNDAY, "Domingo");
    }

    public static String translateDayOfWeektoString(DayOfWeek day) {
        return dayEnumToString.getOrDefault(day, "Día no válido");
    }

    private static final Map<String, DayOfWeek> dayTranslationsInverse = new HashMap<>();

    static {
        dayTranslationsInverse.put("lunes", DayOfWeek.MONDAY);
        dayTranslationsInverse.put("martes", DayOfWeek.TUESDAY);
        dayTranslationsInverse.put("miércoles", DayOfWeek.WEDNESDAY);
        dayTranslationsInverse.put("jueves", DayOfWeek.THURSDAY);
        dayTranslationsInverse.put("viernes", DayOfWeek.FRIDAY);
        dayTranslationsInverse.put("sábado", DayOfWeek.SATURDAY);
        dayTranslationsInverse.put("domingo", DayOfWeek.SUNDAY);
    }

    public static DayOfWeek translateStringToDayOfWeek(String day) {
        DayOfWeek dayOfWeek = dayTranslationsInverse.get(day.toLowerCase());
        if (dayOfWeek == null) {
            throw new InvalidBodyException("Día en español no válido: " + day);
        }
        return dayOfWeek;
    }

    public static List<TutorScheduleDTO> toListDTO(List<TutorSchedule> tutorSchedules) {
        List<TutorScheduleDTO> tutorScheduleDTOs = new ArrayList<>();
        for (TutorSchedule tutorSchedule : tutorSchedules) {
            tutorScheduleDTOs.add(TutorScheduleDTO.builder()
                            .day(translateDayOfWeektoString(tutorSchedule.getDay()))
                            .startTime(tutorSchedule.getStartTime())
                            .endTime(tutorSchedule.getEndTime())
                            .build());
        }
        return tutorScheduleDTOs;
    }

}
