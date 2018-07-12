package banking.system.account;

import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.common.profits.ProfitsRepository;
import banking.system.exceptions.ClientNotActivatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository repository;
    private ClientRepository clientRepository;
    private ProfitsRepository profitsRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository repository, ClientRepository clientRepository, ProfitsRepository profitsRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.profitsRepository = profitsRepository;
    }

    public AccountServiceImpl() {
    }

    @Override
    public Account findById(Long id) {
        Optional<Account> optionalAccount = repository.findById(id);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new RuntimeException("No account found");
        }
    }

    @Override
    public Account findOneByNumber(String number) {
        Optional<Account> optionalAccount = repository.findOneByNumber(number);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        } else {
            throw new RuntimeException("No account found");
        }
    }

    @Override
    public Set<Account> findAll() {
        return new HashSet<>(repository.findAll());
    }

    @Override
    public void deleteOneById(Long id) {
        repository.delete(id);
    }

    @Override
    public Account createAccount(AccountCreateDTO accountCreateDTO) {
        Account account = new Account();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Client client = clientRepository.findByEmail(email).orElseThrow(ClientNotActivatedException::new);

        account.setOwner(client);
        account.setCurrency(accountCreateDTO.getCurrency());
        account.setNumber(checkAccNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setType(accountCreateDTO.getAccountType());

        if (account.getType().equals(AccountType.FOREIGNCURRENCY)) {
            account.setInterest(BigDecimal.ZERO);
            account.setProvision(profitsRepository
                    .findByName("ForeignCurrencyProvision")
                    .orElseThrow(RuntimeException::new)
                    .getPercent());
            return repository.save(account);
        }

        if (account.getType().equals(AccountType.SAVINGS)) {
            account.setInterest(profitsRepository
                    .findByName("SavingsInterest")
                    .orElseThrow(RuntimeException::new)
                    .getPercent());
            account.setProvision(BigDecimal.ZERO);
            return repository.save(account);
        }
        throw new RuntimeException("Account must have set account type");
    }

    private String checkAccNumber() {
        boolean isNrOk = false;
        String acc = "";
        while (!isNrOk) {
            String accountNumber = generateAccountNumber();
            if (!isAccountExists(accountNumber)) {
                acc = accountNumber;
                isNrOk = true;
            }
        }
        return acc;
    }

    @Override
    public Boolean isAccountExists(String accountNumber) {
        Set<Account> accounts = new HashSet<>(repository.findAll());
        Set<String> numbers = new HashSet<>();
        accounts.forEach(a -> numbers.add(a.getNumber()));
        return numbers.contains(accountNumber);

    }

    @Override
    public String generateAccountNumber() {
        Random rnd = new Random();
        int nr = 10000000 + rnd.nextInt(90000000);
        return "PL".concat(Integer.toString(nr));
    }
}
