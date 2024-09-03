package co.udea.codefact.academic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "academic_program")
public class AcademicProgram {
    
    @Id
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_faculty", nullable = false)
    private Faculty faculty;
}
