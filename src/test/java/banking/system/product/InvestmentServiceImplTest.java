package banking.system.product;

import banking.system.account.*;
import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.common.Currency;
import banking.system.transaction.TransactionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class InvestmentServiceImplTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    InvestmentRepository investmentRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void givenProperInvestment_WhenSavingToDb_ThenOk() {

        Account account = prepareAccount();
        InvestmentDTO dto = new InvestmentDTO();
        dto.setAccountNumber(account.getNumber());
        dto.setCurrency(account.getCurrency());
        dto.setAmount(BigDecimal.TEN);
        dto.setInterest(BigDecimal.ONE);
        InvestmentServiceImpl service = new InvestmentServiceImpl(investmentRepository, accountRepository,transactionRepository);

        //when
        Investment investment = service.createInvestment(dto);

        //then
        Assert.assertNotNull(investment);
    }

    private Account prepareAccount() {
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();

        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);

        return accountService.createAccount(accountCreateDTO);

    }

    private Client prepareClient() {
        Client client = new Client();
        client.setEmail("abc");
        client.setPassword("pass");
        return client;
    }

    private Account prepareBankAccount() {
        AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();

        accountCreateDTO.setOwner(clientRepository.save(prepareClient()));
        accountCreateDTO.setCurrency(Currency.PLN);
        accountCreateDTO.setInterest(BigDecimal.ZERO);
        accountCreateDTO.setProvision(BigDecimal.ZERO);
        accountCreateDTO.setAccountType(AccountType.PERSONAL);

        Account bankAccount = accountService.createAccount(accountCreateDTO);
        bankAccount.setNumber("PL99769997PLN");
        return accountRepository.save(bankAccount);

    }

}