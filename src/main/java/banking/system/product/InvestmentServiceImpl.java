package banking.system.product;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.exceptions.ExistingInvestmentException;
import banking.system.transaction.Transaction;
import banking.system.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    private final Long INVESTMENT_DURATION = 12L;

    private final BigDecimal INTEREST = new BigDecimal("0.04");

    private final String BANK_ACCOUNT_NUMBER_PREFIX = "PL99769997";

    private InvestmentRepository investmentRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired

    public InvestmentServiceImpl(InvestmentRepository investmentRepository,
                                 AccountRepository accountRepository,
                                 TransactionRepository transactionRepository) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Investment findById(Long id) {
        Optional<Investment> optionalInvestment = investmentRepository.findById(id);
        if (optionalInvestment.isPresent()) {
            return optionalInvestment.get();
        } else {
            throw new RuntimeException("No investment found");
        }
    }

    @Override
    public Set<Investment> findAll() {
        return new HashSet<>(investmentRepository.findAll());
    }

    @Override
    public Set<Investment> findByAccountId(Long id) {
        Account account;
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
        } else {
            throw new RuntimeException("No account found");
        }

        return account.getInvestments();
    }


    @Override
    public Investment createInvestment(InvestmentDTO investmentDTO) {
        Investment investment = new Investment();
        Account account;
        Optional<Account> optionalAccount = accountRepository.findOneByNumber(investmentDTO.getAccountNumber());
        if (optionalAccount.isPresent()) {
            account = optionalAccount.get();
        } else {
            throw new RuntimeException("No account found");
        }

        Set<Investment> investments = account.getInvestments();

        if (investments != null && !investments.isEmpty()) {
            for (Investment investment1 : investments) {
                if (investment1.getPayment().getDueDate().isAfter(LocalDateTime.now())) {
                    throw new ExistingInvestmentException();
                }
            }
        }


        investment.setAccount(account);
        investment.setAmount(investmentDTO.getAmount());
        investment.setCurrency(investmentDTO.getCurrency());
        investment.setInterest(INTEREST);

        Transaction payment = new Transaction();
        payment.setDueDate(LocalDateTime.now().plusMonths(INVESTMENT_DURATION));
        payment.setAmount(investment.getAmount().multiply(investment.getInterest()));
        payment.setTitle("Investment payment id " + investment.getUuid());
        payment.setTo(investment.getAccount());
        Account bankAccount;
        Optional<Account> optionalBankAccount = accountRepository.findOneByNumber(BANK_ACCOUNT_NUMBER_PREFIX + investment.getCurrency().name());
        if (optionalBankAccount.isPresent()) {
            bankAccount = optionalBankAccount.get();

        } else {
            throw new RuntimeException("No correct bank account available");
        }
        payment.setFrom(bankAccount);
        payment.setCurrency(investment.getCurrency());
        investment.setPayment(payment);


        Transaction invest = new Transaction();
        invest.setCurrency(investment.getCurrency());
        invest.setAmount(investment.getAmount());
        invest.setDueDate(LocalDateTime.now());
        invest.setFrom(investment.getAccount());
        invest.setTo(bankAccount);
        invest.setTitle("Investment id" + investment.getUuid());
        transactionRepository.save(invest);

        return investmentRepository.save(investment);
    }



    @Override
    public void deleteOneById(Long id) {
        investmentRepository.delete(id);
    }
}
