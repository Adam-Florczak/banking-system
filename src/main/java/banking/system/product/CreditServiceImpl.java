package banking.system.product;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.transaction.Transaction;
import banking.system.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CreditServiceImpl implements CreditService {
    private final String BANKACCOUNTNUMBERPREFIX = "PL99769997";
    private CreditRepository creditRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.creditRepository = creditRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Credit findById(Long id) {
        Optional<Credit> creditOptional = creditRepository.findById(id);
        if (creditOptional.isPresent()) {
            return creditOptional.get();
        } else {
            throw new RuntimeException("No credit with id:" + id + " found");
        }
    }

    @Override
    public Set<Credit> findAll() {
        return new HashSet<>(creditRepository.findAll());
    }

    @Override
    public Set<Credit> findByAccount(Long id) {
        Account account;
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            account = accountOptional.get();
        } else {
            throw new RuntimeException("No credit for Account id:" + id + " found");
        }
        return account.getCredits();
    }

    @Transactional
    @Override
    public Credit createCredit(CreditDTO creditDTO) {
        Credit credit = new Credit();

        Account account = accountRepository.findOneByNumber(creditDTO.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("No account found"));


        if (haveOpenCredit(account)) {
            throw new RuntimeException("This account already has an opened credit");
        }

        credit.setAccount(account);
        credit.setAmount(creditDTO.getAmount());
        credit.setCurrency(creditDTO.getCurrency());
        credit.setInstallmentsQuantity(creditDTO.getInstallmentsQuantity());
        credit.setInterest(creditDTO.getInterest());

        Set<Transaction> instalmentsSet = new HashSet<>();
        BigDecimal installmentAmount = credit.getAmount().multiply(credit.getInterest())
                .divide(new BigDecimal(String.valueOf(credit.getInstallmentsQuantity())), BigDecimal.ROUND_DOWN);
        LocalDateTime paymentDate = LocalDateTime.now();

        Account bankAccount = accountRepository.findOneByNumber(BANKACCOUNTNUMBERPREFIX + credit.getCurrency().name())
                .orElseThrow(() -> new RuntimeException("No correct bank account available"));

        for (int i = 0; i < credit.getInstallmentsQuantity(); i++) {
            paymentDate = paymentDate.plusMonths(1L);
            Transaction installment = new Transaction(credit.getAccount(), bankAccount,
                    paymentDate, credit.getCurrency(), installmentAmount,
                    (i + 1) + " from " + credit.getInstallmentsQuantity() + " loan installment");
            instalmentsSet.add(installment);
        }

        credit.setInstallments(instalmentsSet);
        Transaction grant = new Transaction();
        grant.setTitle("Grant from credit " + credit.getUuid());
        grant.setFrom(bankAccount);
        grant.setTo(account);
        grant.setCurrency(credit.getCurrency());
        grant.setAmount(credit.getAmount());
        grant.setDueDate(LocalDateTime.now());
        transactionRepository.save(grant);
        return creditRepository.save(credit);
    }

    @Override
    public void deleteOneById(Long id) {
        creditRepository.delete(id);
    }

    private boolean haveOpenCredit(Account account) {
        if(account.getCredits() == null){
            return false;
        }

        return account.getCredits().stream()
                .flatMap(credit -> credit.getInstallments().stream())
                .max(Comparator.comparing(Transaction::getDueDate))
                .map(transaction -> transaction.getDueDate()
                .isBefore(LocalDateTime.now()))
                .orElse(false);
    }
}
