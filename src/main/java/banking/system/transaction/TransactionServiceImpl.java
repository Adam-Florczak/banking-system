package banking.system.transaction;

import banking.system.account.Account;
import banking.system.account.AccountRepository;
import banking.system.exceptions.*;
import banking.system.exchangerate.ExchangeRateRepository;
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

    private final String bankAccountNumberPrefix="PL99769997";

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private ExchangeRateRepository exchangeRateRepository;

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
        String fromNumber = transactionDTO.getFromNumber();
        String toNumber = transactionDTO.getToNumber();

        if(transactionDTO.getDueDate().isBefore(LocalDateTime.now())){
            throw new PastTransactionException();
        }
        if(fromNumber.equals(toNumber)){
            throw new SendToOwnAccountException();
        }

        Account from = accountRepository.findOneByNumber(fromNumber).orElseThrow(RuntimeException::new);
        Account to = accountRepository.findOneByNumber(toNumber).orElseThrow(RuntimeException::new);

        Boolean differentFromAndTransactionCurrency=false;
        if(!from.getCurrency().equals(transactionDTO.getCurrency())){
            differentFromAndTransactionCurrency=true;
        }
        Boolean differentTransactionAndToCurrency=false;
        if(!transactionDTO.getCurrency().equals(to.getCurrency())){
            differentTransactionAndToCurrency=true;
        }
        BigDecimal fromToTransactionRate=BigDecimal.ONE;
        BigDecimal transactionToToRate=BigDecimal.ONE;
        if(differentFromAndTransactionCurrency){
            fromToTransactionRate=exchangeRateRepository
                    .findFirstByFromAndToOrderByCreatedAtDesc(from.getCurrency(),transactionDTO.getCurrency())
                    .getRate();
        }
        if(differentTransactionAndToCurrency){
            transactionToToRate=exchangeRateRepository
                    .findFirstByFromAndToOrderByCreatedAtDesc(transactionDTO.getCurrency(),to.getCurrency())
                    .getRate();
        }

        BigDecimal moneyNeeded;
        if(differentFromAndTransactionCurrency){
            moneyNeeded=transactionDTO.getAmount().multiply(fromToTransactionRate);
        }
        else{
            moneyNeeded=transactionDTO.getAmount();
        }
        if(from.getBalance().compareTo(moneyNeeded)<0){
            throw new NoEnoughMoneyException();
        }

        if(transactionDTO.getAmount().compareTo(BigDecimal.ZERO)<0){
            throw new NegativeAmountException();
        }

        if(transactionDTO.getTitle().equals("")){
            throw new NotEmptyTitleFieldException();
        }

        if(differentFromAndTransactionCurrency){
            BigDecimal amountInCorrectCurrency= transactionDTO.getAmount().multiply(fromToTransactionRate);
            from.setBalance(from.getBalance().subtract(amountInCorrectCurrency.multiply(new BigDecimal("1.05"))));
            takeProvision(amountInCorrectCurrency.multiply(new BigDecimal("0.05")),
                    bankAccountNumberPrefix+transactionDTO.getCurrency().name());
        }
        else{
            from.setBalance(from.getBalance().subtract(transactionDTO.getAmount()));
        }

        if(differentTransactionAndToCurrency){
            BigDecimal amountInCorrectCurrency = transactionDTO.getAmount().multiply(transactionToToRate);
            to.setBalance(to.getBalance().add(amountInCorrectCurrency.multiply(new BigDecimal("0.95"))));
            takeProvision(amountInCorrectCurrency.multiply(new BigDecimal("0.05")),
                    bankAccountNumberPrefix+transactionDTO.getCurrency());
        }
        else{
            to.setBalance(to.getBalance().add(transactionDTO.getAmount()));
        }

        Transaction transaction = new Transaction();
        transaction.setFrom(from);
        transaction.setTo(to);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTitle(transactionDTO.getTitle());
        transaction = transactionRepository.save(transaction);

        accountRepository.save(from);
        accountRepository.save(to);

        return transaction;
    }

    @Override
    public void deleteOneById(Long id) {
        transactionRepository.delete(id);
    }

    private void takeProvision(BigDecimal value, String bankAccountNumber){
        Account bankAccount=accountRepository.findOneByNumber(bankAccountNumber).orElseThrow(RuntimeException::new);
        bankAccount.setBalance(bankAccount.getBalance().add(value));
    }
}
