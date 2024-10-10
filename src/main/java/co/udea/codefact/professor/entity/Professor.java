package co.udea.codefact.professor.entity;

import co.udea.codefact.subject.entity.Subject;
import co.udea.codefact.user.entity.User;
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

@Entity
@Table(name = "professor")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Professor {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_subject", nullable = true)
    private Subject subject;

    public boolean hasSubject() {
        return this.subject != null;
    }

}
