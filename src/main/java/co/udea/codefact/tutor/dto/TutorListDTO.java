package co.udea.codefact.tutor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TutorListDTO {

    private String name;
    private String username;
}
