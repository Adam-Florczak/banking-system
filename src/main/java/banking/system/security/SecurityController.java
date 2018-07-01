package banking.system.security;

import banking.system.client.Client;
import banking.system.client.ClientCreateDTO;
import banking.system.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SecurityController {

    private ClientService clientService;

    @Autowired
    public SecurityController(ClientService clientService){
        this.clientService = clientService;
    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Client client = new Client();
        modelAndView.addObject("client", client);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid ClientCreateDTO client, BindingResult bindingResult) {
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

}
