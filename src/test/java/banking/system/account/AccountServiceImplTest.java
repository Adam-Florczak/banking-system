package banking.system.account;

import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.common.Currency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountServiceImpl accountServiceImpl;


    private Account prepareAccount() {
        Account account = new Account();
        account.setType(AccountType.PERSONAL);
        account.setCurrency(Currency.PLN);
        account.setOwner(clientRepository.save(prepareClient()));
        account.setNumber("98237");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private Client prepareClient() {
        Client client = new Client();
        client.setEmail("abc");
        client.setPassword("pass");
        return client;
    }

    @Test
    public void givenNewAccountEntity_WhenSavedAndRetrievingFromDb_ThenOk() {
        accountRepository.save(prepareAccount());
        Optional<Account> found = accountRepository.findById(1L);

        Assert.assertTrue(found.isPresent());
    }


}
