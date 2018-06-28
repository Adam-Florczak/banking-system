package banking.system.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService  {

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void sendRegistrationMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your new shiny bank registration");
        message.setText("Thanks for registration\n " +
                "here will be some data obtained from user class and\n" +
                " other funny things");
        emailSender.send(message);
    }
}
