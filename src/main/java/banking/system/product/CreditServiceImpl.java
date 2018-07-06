package banking.system.product;
import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.account.AccountType;
import banking.system.transaction.Transaction;
import banking.system.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CreditServiceImpl implements CreditService{
    private final String bankAccountNumberPrefix="PL99769997";
    private CreditRepository creditRepository;
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.creditRepository = creditRepository;
        this.accountRepository=accountRepository;
        this.transactionRepository=transactionRepository;
    }

    @Override
    public Credit findById(Long id) {
        Optional<Credit> creditOptional= creditRepository.findById(id);
        if(creditOptional.isPresent()){
            return creditOptional.get();
        }
        else{
            throw new RuntimeException("No credit with id:"+id+" found");
        }
    }

    @Override
    public Set<Credit> findAll() {
        return new HashSet<>(creditRepository.findAll());
    }

    @Override
    public Credit findByAccount(Long id) {
        Optional<Credit> creditOptional=findAll().stream().filter(credit->credit.getAccount().getId().equals(id)).findFirst();
        if(creditOptional.isPresent()){
            return creditOptional.get();
        }
        else{
            throw new RuntimeException("No credit for Account id:"+id+" found");
        }
    }

    @Transactional
    @Override
    public Credit createTransaction(CreditDTO creditDTO) {
        Credit credit=new Credit();
        Optional<Account> optionalAccount=accountRepository.findOneByNumber(creditDTO.getAccountNumber());
        Account account;
        if(optionalAccount.isPresent()){
            account=optionalAccount.get();
        }
        else{
            throw new RuntimeException("No account found");
        }

        credit.setAccount(account);
        credit.setAmount(creditDTO.getAmount());
        credit.setCurrency(creditDTO.getCurrency());
        credit.setInstallmentsQuantity(creditDTO.getInstallmentsQuantity());
        credit.setInterest(creditDTO.getInterest());

        Set<Transaction> instalmentsSet=new HashSet<>();
        BigDecimal installmentAmount=credit.getAmount().multiply(credit.getInterest())
                .divide(new BigDecimal(String.valueOf(credit.getInstallmentsQuantity())),BigDecimal.ROUND_DOWN);
        LocalDateTime paymentDate=LocalDateTime.now();

        Account bankAccount;
        Optional<Account> optionalBankAccount=accountRepository.findOneByNumber(bankAccountNumberPrefix+credit.getCurrency().name());
        if(optionalBankAccount.isPresent()){
            bankAccount=optionalBankAccount.get();

        }
        else{
            throw new RuntimeException("No correct bank account available");
        }

        for(int i=0;i<credit.getInstallmentsQuantity();i++){
            paymentDate=paymentDate.plusMonths(1L);

            instalmentsSet.add(new Transaction(credit.getAccount(),bankAccount,paymentDate,credit.getCurrency(),installmentAmount,
                    (i+1)+" from "+credit.getInstallmentsQuantity()+" loan installment"));
        }
        credit.setInstallments(instalmentsSet);
        Transaction grant=new Transaction();
        grant.setTitle("Grant from credit "+credit.getUuid());
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
}
