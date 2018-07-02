package banking.system.transaction;

import java.util.Set;

public interface TransactionService {
    Transaction findById(Long id);


    Set<Transaction> findAll();
    Set<Transaction> findAllOnAccount(Long id);
    Set<Transaction> findAllFromAccount(Long id);
    Set<Transaction> findAllToAccount(Long id);

    Transaction createTransaction(Transaction transaction);

    void deleteOneById(Long id);
}
