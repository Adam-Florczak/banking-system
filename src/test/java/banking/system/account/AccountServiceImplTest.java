package banking.system.account;

import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.common.Currency;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceImplTest {

    @Autowired
    AccountRepository repository;

    @Autowired
    ClientRepository clientRepository;


    private Account prepareAccount() {
        Account account = new Account();
        account.setType(AccountType.PERSONAL);
        account.setCurrency(Currency.PLN);
        account.setOwner(clientRepository.save(prepareClient()));
        account.setNumber("1234");
        account.setBalance(BigDecimal.ZERO);
        return account;
    }

    private Client prepareClient() {
        Client client = new Client();
        client.setEmail("abc");
        client.setPassword("pass");
        return client;
    }
//
//    @Test
//    public void contextLoads() {
//    }

    @Test
    public void givenNewAccountEntity_WhenSavedAndRetrievingFromDb_ThenOk() {
        repository.save(prepareAccount());
        Optional<Account> found = repository.findById(1L);

        Assert.assertTrue(found.isPresent());
    }


}
