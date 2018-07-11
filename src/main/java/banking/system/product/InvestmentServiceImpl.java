package banking.system.product;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.exceptions.ExistingInvestmentException;
import banking.system.transaction.Transaction;
import banking.system.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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
    private EntityManager entityManager;

    @Autowired

    public InvestmentServiceImpl(InvestmentRepository investmentRepository,
                                 AccountRepository accountRepository,
                                 TransactionRepository transactionRepository,
                                 EntityManager entityManager) {
        this.investmentRepository = investmentRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Investment findById(Long id) {
        return investmentRepository.findById(id).orElseThrow(() -> new RuntimeException("No investment found"));
    }

    @Override
    public Set<Investment> findAll() {
        return new HashSet<>(investmentRepository.findAll());
    }

    @Override
    public Set<Investment> findByAccountId(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No account found"))
                .getInvestments();
    }


    @Override
    public Investment createInvestment(InvestmentDTO investmentDTO) {
        Account investmentOwner = accountRepository.findOneByNumber(investmentDTO.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("No account found"));


        Set<Investment> investments = investmentOwner.getInvestments();

        if (investments != null && !investments.isEmpty()) {
            for (Investment investment1 : investments) {
                if (investment1.getPayment().getDueDate().isAfter(LocalDateTime.now())) {
                    throw new ExistingInvestmentException();
                }
            }
        }


        Investment investment = Investment.builder()
                .withAccount(investmentOwner)
                .withAmount(investmentDTO.getAmount())
                .withCurrency(investmentDTO.getCurrency())
                .withInterest(INTEREST)
                .build();

        Account bankAccount = accountRepository.findOneByNumber(BANK_ACCOUNT_NUMBER_PREFIX+investment.getCurrency().name())
                .orElseThrow(() -> new RuntimeException("No correct bank account available"));

        Transaction outputTransferWithProfit = new Transaction();
        outputTransferWithProfit.setDueDate(LocalDateTime.now().plusMonths(INVESTMENT_DURATION));
        outputTransferWithProfit.setAmount(investment.getAmount().multiply(investment.getInterest()));
        outputTransferWithProfit.setTitle("Investment payment id " + investment.getUuid());
        outputTransferWithProfit.setTo(investment.getAccount());
        outputTransferWithProfit.setFrom(bankAccount);
        outputTransferWithProfit.setCurrency(investment.getCurrency());
        outputTransferWithProfit = transactionRepository.save(outputTransferWithProfit);
        investment.setPayment(outputTransferWithProfit);


        Transaction initializingTransfer = new Transaction();
        initializingTransfer.setCurrency(investment.getCurrency());
        initializingTransfer.setAmount(investment.getAmount());
        initializingTransfer.setDueDate(LocalDateTime.now());
        initializingTransfer.setFrom(investment.getAccount());
        initializingTransfer.setTo(bankAccount);
        initializingTransfer.setTitle("Investment id " + investment.getUuid());
        transactionRepository.save(initializingTransfer);

        Investment save = investmentRepository.save(investment);
        entityManager.refresh(save);
        return save;
    }



    @Override
    public void deleteOneById(Long id) {
        investmentRepository.delete(id);
    }
}
