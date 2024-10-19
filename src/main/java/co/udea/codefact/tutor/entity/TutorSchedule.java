package co.udea.codefact.tutor.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "tutor_schedule")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TutorSchedule {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tutor")
    private Tutor tutor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private DayOfWeek day;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}
