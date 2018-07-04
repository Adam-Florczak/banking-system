package banking.system.account;

import java.util.Set;

public interface AccountService {

    Account findById(Long id);

    Account findOneByNumber(String number);

    Set<Account> findAll();

    void deleteOneById(Long id);

    Account createAccount(AccountCreateDTO accountCreateDTO);

    Boolean isAccountExists(String accountNumber);

    String generateAccountNumber();
}
