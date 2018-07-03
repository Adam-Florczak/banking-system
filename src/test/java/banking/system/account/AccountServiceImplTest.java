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
import org.springframework.test.context.ContextConfiguration;
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


    private AccountCreateDTO prepareAccountDto(AccountServiceImpl accountService) {

        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setNumber(accountService.generateAccountNumber());
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);
        return accountCreateDTO;
    }

    private Client prepareClient() {
        Client client = new Client();
        client.setEmail("abc");
        client.setPassword("pass");
        return client;
    }

    @Test
    public void givenNewAccountEntity_WhenSavedAndRetrievingFromDb_ThenOk() {

        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);

        AccountCreateDTO accountCreateDTO = prepareAccountDto(accountService);

        accountService.createAccount(accountCreateDTO);

        Account account = accountService.findById(1L);

        Assert.assertNotNull(account);

    }


}
