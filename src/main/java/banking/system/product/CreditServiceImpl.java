package banking.system.product;
import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.account.AccountType;
import banking.system.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

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

    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository, AccountRepository accountRepository) {
        this.creditRepository = creditRepository;
        this.accountRepository=accountRepository;
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
        for(int i=0;i<credit.getInstallmentsQuantity();i++){
            paymentDate=paymentDate.plusMonths(1L);
            Account bankAccount;
            Optional<Account> optionalBankAccount=accountRepository.findOneByNumber(bankAccountNumberPrefix+credit.getCurrency().name());
            if(optionalBankAccount.isPresent()){
                bankAccount=optionalBankAccount.get();
            }
            else{
                throw new RuntimeException("No correct bank account available");
            }
            instalmentsSet.add(new Transaction(credit.getAccount(),bankAccount,paymentDate,credit.getCurrency(),installmentAmount,
                    (i+1)+" from "+credit.getInstallmentsQuantity()+" loan installment"));
        }
        credit.setInstallments(instalmentsSet);

        return creditRepository.save(credit);

    }

    @Override
    public void deleteOneById(Long id) {
        creditRepository.delete(id);
    }
}
