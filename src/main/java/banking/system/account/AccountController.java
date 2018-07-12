package banking.system.account;

import banking.system.client.Client;
import banking.system.client.ClientService;
import banking.system.common.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@RestController
public class AccountController {

    private ClientService clientService;
    private AccountService accountService;

    @Autowired
    public AccountController(ClientService clientService, AccountService accountService) {
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @GetMapping("/createAccount")
    public ModelAndView accountCreation(){
        ModelAndView modelAndView = new ModelAndView();
        AccountCreateDTO createDTO = new AccountCreateDTO();

        modelAndView.addObject("createDTO", createDTO);
        modelAndView.setViewName("createAccount");
        return modelAndView;
    }

    //TODO: Interest and provision rules
    @PostMapping("/createAccount")
    public ModelAndView createNewAccount(AccountCreateDTO createDTO){
        ModelAndView modelAndView = new ModelAndView();

        accountService.createAccount(createDTO);
        modelAndView.addObject("successMessage", "Account has been created");
        modelAndView.setViewName("accountPage");

        return modelAndView;
    }
}
