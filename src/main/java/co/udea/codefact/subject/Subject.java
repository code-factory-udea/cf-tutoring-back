package co.udea.codefact.subject;

import co.udea.codefact.academic.AcademicProgram;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subject")
public class Subject {
    
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "academic_program_id")
    private AcademicProgram academicProgram;
}
