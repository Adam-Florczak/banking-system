package banking.system.registration;

import banking.system.client.Client;
import banking.system.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private ClientService service;
    private MessageSource messages;
    private JavaMailSender mailSender;

    @Autowired
    public RegistrationListener(ClientService service, MessageSource messages, JavaMailSender mailSender) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

    }

    private void confirmRegistration(OnRegistrationCompleteEvent event){
        Client client = event.getClient();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(client, token);

        String recipientAddress = client.getEmail();
        String subsject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() +
                "/registrationConfirm.html?token=" + token;
        String message = messages.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subsject);
        email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
