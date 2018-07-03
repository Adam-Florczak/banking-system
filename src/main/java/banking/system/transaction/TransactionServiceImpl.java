package banking.system.transaction;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Transaction findById(Long id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            return optionalTransaction.get();
        } else {
            throw new RuntimeException("No transaction found");
        }
    }

    @Override
    public Set<Transaction> findAll() {
        return new HashSet<>(transactionRepository.findAll());
    }

    @Override
    public Set<Transaction> findAllFromAccount(Long id) {
        return findAll().stream()
                .filter(transaction -> transaction.getFrom().getId().equals(id))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Transaction> findAllToAccount(Long id) {
        return findAll().stream()
                .filter(transaction -> transaction.getTo().getId().equals(id))
                .collect(Collectors.toSet());
    }

    public Set<Transaction> findAllOnAccount(Long id) {
        Set<Transaction> result = findAllFromAccount(id);
        result.addAll(findAllToAccount(id));
        return result;

    }

    @Transactional
    @Override
    public Transaction createTransaction(TransactionDTO transactionDTO) {

//        Long fromID = transactionDTO.getFrom().getId();
//        Long toID = transactionDTO.getTo().getId();

        String fromNumber = transactionDTO.getFromNumber();
        String toNumber = transactionDTO.getToNumber();


        if(transactionDTO.getDueDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("No option for create past transaction");
        }
        if(fromNumber.equals(toNumber)){
            throw new RuntimeException("No self-transaction allow");
        }

        Account from = accountRepository.findOneByNumber(fromNumber).orElseThrow(RuntimeException::new);
        Account to = accountRepository.findOneByNumber(toNumber).orElseThrow(RuntimeException::new);

        if(transactionDTO.getAmount().compareTo(from.getBalance())>0){
            throw new RuntimeException("You're too poor exception");
        }

        if(transactionDTO.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new RuntimeException("Negative amount exception");
        }

        if(transactionDTO.getTitle().equals("")){
            throw new RuntimeException("No title transaction exception");
        }

        //Todo Co z walutami?
        if(!from.getCurrency().equals(transactionDTO.getCurrency())){
            //todo prowizja1

        }
        if(!transactionDTO.getCurrency().equals(to.getCurrency())){
            //todo proizja2

        }

        Transaction transaction = new Transaction();
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTitle(transactionDTO.getTitle());
        transaction = transactionRepository.save(transaction);

        from = accountRepository.findOneByNumber(fromNumber).orElseThrow(RuntimeException::new);
        to = accountRepository.findOneByNumber(toNumber).orElseThrow(RuntimeException::new);
        from.setBalance(from.getBalance().subtract(transaction.getAmount()));
        to.setBalance(from.getBalance().add(transaction.getAmount()));
        accountRepository.save(from);
        accountRepository.save(to);

        return transaction;
    }

    @Override
    public void deleteOneById(Long id) {
        transactionRepository.delete(id);
    }
}
