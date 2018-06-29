package banking.system.registration;

import banking.system.client.Client;
import banking.system.client.ClientCreateDTO;
import banking.system.client.ClientService;
import banking.system.exception.EmailAlreadyRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;

@Controller
@RequestMapping(value = "/register")
public class RegistrationController {


    private ClientService clientService;

    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(ClientService clientService, ApplicationEventPublisher eventPublisher) {
        this.clientService = clientService;
        this.eventPublisher = eventPublisher;
    }

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
