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
import java.nio.channels.AcceptPendingException;

@DataJpaTest
@RunWith(SpringRunner.class)
public class CreditServiceImplTest {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void givenProperCredit_WhenSavedAndRetrievedFromDb_ThenSavingAndSavedAmountIsEqual() {

        Account account = prepareAccount();

        CreditDTO dto = new CreditDTO();
        dto.setAccountNumber(account.getNumber());
        dto.setAmount(new BigDecimal("1000"));
        dto.setCurrency(Currency.PLN);
        dto.setInstallmentsQuantity(12);
        dto.setInterest(new BigDecimal(2));

        CreditServiceImpl service = new CreditServiceImpl(creditRepository, accountRepository, transactionRepository);

        //when
        Credit saved = service.createCredit(dto);

        //then
        Assert.assertEquals(saved.getAmount(), dto.getAmount());

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

}