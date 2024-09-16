package co.udea.codefact.appointment.entity;

import java.time.LocalDateTime;

import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "waiting_list")
public class WaitingList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="student_id", nullable=false)
    private User student;

    @ManyToOne
    @JoinColumn(name="tutor_id", nullable=false)
    private Tutor tutor;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;


    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}
