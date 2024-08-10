package co.udea.codefact.tutor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tutor_schedule")
public class TutorSchedule {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tutor")
    private Tutor tutor;

    @Column(name = "day")
    private String day;

    @Column(name = "initial_hour")
    private String initialHour;
    
}
