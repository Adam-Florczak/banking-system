package banking.system.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository repository;

    @Autowired
    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
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
        account.setOwner(accountCreateDTO.getOwner());
        account.setCurrency(accountCreateDTO.getCurrency());

        boolean isNrOk = false;
        while (!isNrOk) {
            String accountNumber = generateAccountNumber();
            if (!isAccountExists(accountNumber)) {
                account.setNumber(accountNumber);
                isNrOk = true;
            }
        }

        account.setType(accountCreateDTO.getAccountType());
        account.setInterest(accountCreateDTO.getInterest());
        account.setProvision(accountCreateDTO.getProvision());

        return repository.save(account);
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
