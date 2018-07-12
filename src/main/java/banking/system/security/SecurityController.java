package banking.system.security;

import banking.system.client.Client;
import banking.system.client.ClientCreateDTO;
import banking.system.client.ClientService;
import banking.system.exceptions.VerificationTokenExpiredException;
import banking.system.security.token.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Calendar;

@RestController
public class SecurityController {

    private ClientService clientService;

    @Autowired
    public SecurityController(ClientService clientService) {
        this.clientService = clientService;

    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value={"/", "/hello"}, method = RequestMethod.GET)
    public ModelAndView accountPage(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientService.findClientByEmail(auth.getName());
        modelAndView.addObject("userName", client.getFirstName() + " " + client.getLastName());
        modelAndView.addObject("accNumber", client.getAccountSet().iterator().next().getNumber());
        modelAndView.setViewName("accountPage");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
//        Client client = new Client();
        ClientCreateDTO clientDTO = new ClientCreateDTO();
        modelAndView.addObject("clientDTO", clientDTO);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid ClientCreateDTO client,
                                      BindingResult bindingResult,
                                      WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Client clientExists = clientService.findClientByEmail(client.getEmail());
        if (clientExists != null) {
            bindingResult
                    .rejectValue("email", "error.client",
                            "There is already a client registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            clientService.saveClient(client);
            modelAndView.addObject("successMessage", "Client has been registered successfully");
            modelAndView.addObject("client", new Client());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientService.findClientByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + client.getFirstName() + " " + client.getLastName() + " (" + client.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping("/registerConfirm")
    public ModelAndView confirmRegister(@RequestParam("token") String token){
        ModelAndView modelAndView = new ModelAndView();
        VerificationToken verificationToken = clientService.getVerificationToken(token);

        if(verificationToken == null){
            throw new VerificationTokenExpiredException();
        }

        Client client = clientService.findByToken(token);
        Calendar cal = Calendar.getInstance();

        if((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0){
            throw new VerificationTokenExpiredException();
        }

        client.setEnabled(true);
        clientService.saveRegisteredClient(client);
        modelAndView.addObject("activateMessage", "Account activated");
        modelAndView.setViewName("login");
        return modelAndView;
    }

}
