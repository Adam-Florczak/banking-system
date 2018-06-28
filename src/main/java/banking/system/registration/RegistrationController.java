package banking.system.registration;

import org.springframework.beans.factory.annotation.Autowired;
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


    private ClientService userService;

    @Autowired
    RegistrationController(ClientService clientService){
        this.ClientService = clientService;
    }

    @ModelAttribute("user")
    public UserCreateDTO userRegistration() {
        return new UserCreateDTO();
    }

@GetMapping
public String showRegistrationform(Model model){
        model.addAttribute("user",new UserCreateDTO());
    return "/registration";
}

    @PostMapping(value = "/newclient")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserCreateDto userCreateDTO,
            BindingResult result,
            WebRequest request,
            Errors errors) {

        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(userCreateDTO, result);
        }
        if (registered == null) {
            result.rejectValue("email", "message.regError");
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", userCreateDTO);
        }
        else {
            return new ModelAndView("successRegister", "user", userCreateDTO);
        }
    }
    private User createUserAccount(UserCreateDTO accountDto, BindingResult result) {
        User registered = null;
        try {
            registered = service.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            return null;
        }
        return registered;
    }



}
