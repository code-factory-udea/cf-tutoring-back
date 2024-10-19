package co.udea.codefact.tutor.entity;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tutor")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Tutor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "username")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "id_subject", nullable = true)
    private Subject subject;

    @Column(name = "virtual_meeting_link", nullable = true)
    private String virtualMeetingLink;

    @Column(name = "is_active")
    private boolean isActive;

}
