package co.udea.codefact.subject;

import co.udea.codefact.academic.AcademicProgram;
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

@Entity
@Table(name = "subject")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subject {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private Long code;

    @Column(name = "name", length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "academic_program_id")
    private AcademicProgram academicProgram;
}
