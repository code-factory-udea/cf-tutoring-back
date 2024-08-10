package co.udea.codefact.subject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
