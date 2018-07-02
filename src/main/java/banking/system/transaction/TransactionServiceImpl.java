package banking.system.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{

    TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }

    @Override
    public Transaction findById(Long id) {
        Optional<Transaction> optionalTransaction= transactionRepository.findById(id);
        if(optionalTransaction.isPresent()){
            return optionalTransaction.get();
        }
        else{
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
    public Set<Transaction> findAllToAccount(Long id){
        return findAll().stream()
                .filter(transaction -> transaction.getTo().getId().equals(id))
                .collect(Collectors.toSet());
    }

    public Set<Transaction> findAllOnAccount(Long id){

        Set<Transaction> result=findAllFromAccount(id);
        result.addAll(findAllToAccount(id));
        return result;

    }



    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteOneById(Long id) {
        transactionRepository.delete(id);
    }
}
