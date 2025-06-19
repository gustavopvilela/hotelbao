package hotelbao.backend.service;

import hotelbao.backend.dto.EmailDTO;
import hotelbao.backend.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    private JavaMailSender mailSender; // Ignora o erro e vai na fé que vai dar certo!!!

    public void sendEmail (EmailDTO dto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(dto.getTo());
            message.setSubject(dto.getSubject());
            message.setText(dto.getBody());
            mailSender.send(message);
        }
        catch (MailSendException e) {
            throw new EmailException(e.getMessage());
        }
    }
}
