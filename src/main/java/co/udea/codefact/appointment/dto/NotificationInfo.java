package co.udea.codefact.appointment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationInfo {

    private String subject;
    private String body;
    private String recipient;
}
