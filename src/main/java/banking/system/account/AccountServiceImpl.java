package banking.system.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
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

        return null;
    }
}
