package banking.system.product;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
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

    private final Long investmentDuration=12L;
    private final BigDecimal interest=new BigDecimal("0.04");
    private final String bankAccountNumberPrefix="PL99769997";

    private InvestmentRepository investmentRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public InvestmentServiceImpl(InvestmentRepository investmentRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Investment findById(Long id) {
        Optional<Investment> optionalInvestment= investmentRepository.findById(id);
        if(optionalInvestment.isPresent()){
            return optionalInvestment.get();
        }
        else{
            throw new RuntimeException("Np investment found");
        }
    }

    @Override
    public Set<Investment> findAll() {
        return new HashSet<>(investmentRepository.findAll());
    }

    @Override
    public Investment findByAccount(Long id) {
        Account account;
        Optional<Account> optionalAccount=accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            account=optionalAccount.get();
        }
        else{
            throw new RuntimeException("No account found");
        }
        //todo fixthisshit
        return account.getInvestment();
    }


    @Override
    public Investment createInvestment(InvestmentDTO investmentDTO) {
        Investment investment=new Investment();
        Account account;
        Optional<Account> optionalAccount=accountRepository.findOneByNumber(investmentDTO.getAccountNumber());
        if(optionalAccount.isPresent()){
            account=optionalAccount.get();
        }
        else{
            throw new RuntimeException("No account found");
        }
        investment.setAccount(account);
        investment.setAmount(investmentDTO.getAmount());
        investment.setCurrency(investmentDTO.getCurrency());
        investment.setInterest(interest);

        Transaction payment=new Transaction();
        payment.setDueDate(LocalDateTime.now().plusMonths(investmentDuration));
        payment.setAmount(investment.getAmount().multiply(investment.getInterest()));
        payment.setTitle("Investment payment id"+investment.getUuid());
        payment.setTo(investment.getAccount());
        Account bankAccount;
        Optional<Account> optionalBankAccount=accountRepository.findOneByNumber(bankAccountNumberPrefix+investment.getCurrency().name());
        if(optionalBankAccount.isPresent()){
            bankAccount=optionalBankAccount.get();

        }
        else{
            throw new RuntimeException("No correct bank account available");
        }
        payment.setFrom(bankAccount);
        payment.setCurrency(investment.getCurrency());
        investment.setPayment(payment);

        Transaction invest=new Transaction();
        invest.setCurrency(investment.getCurrency());
        invest.setAmount(investment.getAmount());
        invest.setDueDate(LocalDateTime.now());
        invest.setFrom(investment.getAccount());
        invest.setTo(bankAccount);
        invest.setTitle("Investment id"+investment.getUuid());
        transactionRepository.save(invest);

        return investmentRepository.save(investment);
    }

    @Override
    public void deleteOneById(Long id) {
        investmentRepository.delete(id);
    }
}
