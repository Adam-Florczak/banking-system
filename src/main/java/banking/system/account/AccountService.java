package banking.system.account;

import java.util.Set;

public interface AccountService {

    Account findById(Long id);

    Set<Account> findAll();

    void deleteOneById(Long id);

    Account createAccount(AccountCreateDTO accountCreateDTO);
}
