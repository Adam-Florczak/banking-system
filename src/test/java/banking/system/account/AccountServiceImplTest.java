package banking.system.account;

import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.common.Currency;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;

    private Client prepareClient() {
        Client client = new Client();
        client.setEmail("abc");
        client.setPassword("pass");
        return client;
    }

    @Test
    public void givenCorrectPersonalAccount_WhenSavedAndRetrievingFromDb_ThenNotNull() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);

        //when
        Account saved = accountService.createAccount(accountCreateDTO);
        Account account = accountService.findById(saved.getId());

        //then
        Assert.assertNotNull(account);
    }




    @Test
    public void givenCorrectAccount_WhenSavedDeletedAndRetrievingFromDb_ThenThrowsException() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);

        //when
        Account saved = accountService.createAccount(accountCreateDTO);
        Long id = saved.getId();
        accountService.deleteOneById(id);

        //then
        expectedException.expect(RuntimeException.class);
        accountService.findById(id);
    }

    @Test
    public void givenPersonalAccount_WhenForeignCurrencySet_ThenThrowsException() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);

        //when
        accountCreateDTO.setCurrency(Currency.CHF);

        //then
        expectedException.expect(RuntimeException.class);
        accountService.createAccount(accountCreateDTO);
    }

    @Test
    public void givenPersonalAccount_WhenProvisionBiggerThanZeroSet_ThenThrowsException() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);
        accountCreateDTO.setCurrency(Currency.PLN);

        //when
        accountCreateDTO.setProvision(BigDecimal.ONE);

        //then
        expectedException.expect(RuntimeException.class);
        accountService.createAccount(accountCreateDTO);
    }

    @Test
    public void givenPersonalAccount_WhenInterestBiggerThanZeroSet_ThenThrowsException() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);
        accountCreateDTO.setCurrency(Currency.PLN);

        //when
        accountCreateDTO.setInterest(BigDecimal.ONE);

        //then
        expectedException.expect(RuntimeException.class);
        accountService.createAccount(accountCreateDTO);
    }

    @Test
    public void givenCorrectForeignCurrencyAccount_WhenSavedAndRetrievingFromDb_ThenNotNull() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.CHF);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ONE);
        accountCreateDTO.setAccountType(AccountType.FOREIGNCURRENCY);

        //when
        Account saved = accountService.createAccount(accountCreateDTO);
        Account account = accountService.findById(saved.getId());

        //then
        Assert.assertNotNull(account);
    }



    @Test
    public void givenCorrectSavingsAccount_WhenSavedAndRetrievingFromDb_ThenNotNull() {

        //given
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        
        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ONE);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.SAVINGS);

        //when
        Account saved = accountService.createAccount(accountCreateDTO);
        Account account = accountService.findById(saved.getId());

        //then
        Assert.assertNotNull(account);
    }

    @Test
    public void givenAccountWithExistingNumber_WhenCheckingIfNumberAlreadyExists_ThenReturnsOk() {
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        String number = "PL12345678";
        Account account = new Account();
        account.setType(AccountType.PERSONAL);
        account.setCurrency(Currency.PLN);
        account.setOwner(clientRepository.save(prepareClient()));
        account.setNumber(number);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);

        Boolean isExisting = accountService.isAccountExists(number);

        Assert.assertTrue(isExisting);

    }



}
