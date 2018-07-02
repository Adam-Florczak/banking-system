package banking.system.registration;

import banking.system.client.Client;
import banking.system.client.ClientCreateDTO;
import banking.system.client.ClientService;
import banking.system.exception.EmailAlreadyRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;

@Controller
@RequestMapping(value = "/register")
public class RegistrationController {


    private ClientService clientService;

    private ApplicationEventPublisher eventPublisher;

    private MessageSource messages;

    public RegistrationController(ClientService clientService, ApplicationEventPublisher eventPublisher, @Qualifier("messageSource") MessageSource messages) {
        this.clientService = clientService;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
    }

    @Autowired


    @ModelAttribute("client")
    public ClientCreateDTO userRegistration() {
        return new ClientCreateDTO();
    }

    @GetMapping
    public String showRegistrationform(Model model) {
        model.addAttribute("client", new ClientCreateDTO());
        return "/registration";
    }

    @PostMapping(value = "/newclient")
    public ModelAndView registerClientAccount(
            @ModelAttribute("client") @Valid ClientCreateDTO clientCreateDTO,
            BindingResult result,
            WebRequest request,
            Errors errors) {

        Client registered = new Client();
        if (!result.hasErrors()) {
            registered = createClientAccount(clientCreateDTO, result);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "client", clientCreateDTO);
        }
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
                    registered, request.getLocale(), appUrl));
        } catch (Exception me) {
            return new ModelAndView("emailError", "client", clientCreateDTO);
        }
        return new ModelAndView("successRegister", "client", clientCreateDTO);
    }
    //TODO: add exception handling
    public String confirmRegistration(
            WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = clientService.getVerificationToken(token);
        if(verificationToken == null){
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
        Client client = verificationToken.getClient();
        Calendar cal = Calendar.getInstance();
        if((verificationToken.getExpiryDate().getTime()-cal.getTime().getTime())<=0){
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
//        client.setEnabled(true);
        clientService.saveRegisteredClient(client);
        return "redirect:/layout.html?lang=" + request.getLocale().getLanguage();
    }

    private Client createClientAccount(ClientCreateDTO clientCreateDTO, BindingResult result) {
        Client registered = null;
        try {
            registered = clientService.registerNewUserAccount(clientCreateDTO);
        } catch (EmailAlreadyRegisteredException e) {
            return null;
        }
        return registered;
    }

}
