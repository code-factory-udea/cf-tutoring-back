package co.udea.codefact.appointment.service;

import co.udea.codefact.appointment.dto.NotificationInfo;
import co.udea.codefact.appointment.entity.Appointment;
import co.udea.codefact.tutor.entity.Tutor;
import co.udea.codefact.user.entity.User;
import co.udea.codefact.utils.constants.FormatConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationEmailService {

    private final JavaMailSender mailSender;

    public NotificationEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentConfirmationEmail(Appointment appointment) {
        String modality = this.getModality(appointment);
        Tutor tutor = appointment.getTutor();
        User student = appointment.getStudent();
        String recipient = String.format(FormatConstants.EMAIL_FORMAT, student.getUsername());
        String messageContent =
                "<h4>Hola " + student.getFirstName() + ",</h4>"
                        + "<p>¡Tu cita de monitoría ha sido confirmada! A continuación encontrarás los detalles:</p>"
                        + "<ul>"
                        + "<li><strong>📅 Fecha:</strong> " + appointment.getDate().toLocalDate() + "</li>"
                        + "<li><strong>🕒 Hora:</strong> " + appointment.getDate().toLocalTime().toString().substring(0, 5) + "</li>"
                        + "<li><strong>📍 Modalidad:</strong> " + modality + "</li>";
        if (modality.equalsIgnoreCase("Virtual")) {
            messageContent += "<li><strong>🔗 Enlace para la sesión virtual:</strong> "
                    + "<a href='" + tutor.getVirtualMeetingLink() + "'>Haz clic aquí para unirte</a></li>";
        }
        messageContent += "<li><strong>🧑‍🏫 Tutor asignado:</strong> " +
                String.format(FormatConstants.FULLNAME_FORMAT, tutor.getUser().getFirstName(), tutor.getUser().getLastName()) + "</li>"
                + "<li><strong>📧 Correo: </strong> <a href='mailto:" + String.format(FormatConstants.EMAIL_FORMAT, tutor.getUser().getUsername()) + "'>"
                + String.format(FormatConstants.EMAIL_FORMAT, tutor.getUser().getUsername()) + "</a></li>"
                + "</ul>"
                + "<p>Si necesitas cancelar tu cita, puedes hacerlo a través del portal.</p>"
                + "<p>Estamos seguros de que esta monitoría será una experiencia valiosa para tu aprendizaje. ¡Nos vemos pronto!</p>"
                + "<p>Un saludo cordial</p>";

        this.sendNotification(NotificationInfo.builder()
                .body(messageContent)
                .subject(String.format("%s ha confirmado tu cita de monitoría",
                        tutor.getUser().getFirstName()))
                .recipient(recipient)
                .build());
    }

    public void sendAppointmentCancellationByTutorEmail(Appointment appointment) {
            Tutor tutor = appointment.getTutor();
            User student = appointment.getStudent();
            String recipient = String.format(FormatConstants.EMAIL_FORMAT, student.getUsername());
            String messageContent =
                    "<h4>Hola " + student.getFirstName() + ",</h4>"
                            + "<p>Lamentamos informarte que tu cita ha sido <strong>cancelada</strong>. A continuación, los detalles:</p>"
                            + "<ul>"
                            + "<li><strong>📅 Fecha:</strong> " + appointment.getDate().toLocalDate() + "</li>"
                            + "<li><strong>🕒 Hora:</strong> " + appointment.getDate().toLocalTime().toString().substring(0, 5) + "</li>"
                            + "<li><strong>🧑‍🏫 Tutor asignado:</strong> "
                            + String.format(FormatConstants.FULLNAME_FORMAT, tutor.getUser().getFirstName(), tutor.getUser().getLastName()) + "</li>"
                            + "<li><strong>📧 Correo del tutor:</strong> <a href='mailto:"
                            + String.format(FormatConstants.EMAIL_FORMAT, tutor.getUser().getUsername()) + "'>"
                            + String.format(FormatConstants.EMAIL_FORMAT, tutor.getUser().getUsername()) + "</a></li>"
                            + "</ul>"
                            + "<p>Por favor, si deseas reprogramar la cita, no dudes en hacerlo a través del portal.</p>"
                            + "<p>Sentimos las molestias ocasionadas y esperamos poder ayudarte en otra ocasión.</p>"
                            + "<p>Un saludo cordial,</p>";

            this.sendNotification(NotificationInfo.builder()
                    .body(messageContent)
                    .subject(String.format("Notificación: Tu cita de tutoría con %s ha sido cancelada",
                            tutor.getUser().getFirstName()))
                    .recipient(recipient)
                    .build());
    }

    public void sendAppointmentCancellationByStudentEmail(Appointment appointment) {
        User tutor = appointment.getTutor().getUser();
        User student = appointment.getStudent();
        String recipient = String.format(FormatConstants.EMAIL_FORMAT, tutor.getUsername());
        String messageContent =
                "<h4>Hola " + tutor.getFirstName() + ",</h4>"
                        + "<p>El estudiante "+String.format(FormatConstants.FULLNAME_FORMAT, student.getFirstName(), student.getLastName())
                        + " <strong>ha cancelado</strong> la cita de monitoría programada. A continuación, los detalles:</p>"
                        + "<ul>"
                        + "<li><strong>📅 Fecha:</strong> " + appointment.getDate().toLocalDate() + "</li>"
                        + "<li><strong>🕒 Hora:</strong> " + appointment.getDate().toLocalTime().toString().substring(0, 5) + "</li>"
                        + "</ul>"
                        + "<p>Un saludo cordial,</p>";

        this.sendNotification(NotificationInfo.builder()
                .body(messageContent)
                .subject("Notificación: Un estudiante ha cancelado la cita")
                .recipient(recipient)
                .build());
    }

    public void sendNotification(NotificationInfo info) {
        try {
            System.out.println("Entro a sendNotification");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setSubject(info.getSubject());
            helper.setTo(String.format(FormatConstants.EMAIL_FORMAT, info.getRecipient()));
            helper.setText(info.getBody(), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getModality(Appointment appointment) {
        return appointment.isVirtual() ? "Virtual" : "Presencial";
    }

}
