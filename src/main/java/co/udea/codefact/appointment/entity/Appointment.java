package co.udea.codefact.appointment.entity;

import java.time.LocalDateTime;

import co.udea.codefact.appointment.utils.AppointmentStatus;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "is_virtual")
    private boolean isVirtual;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
    
}
