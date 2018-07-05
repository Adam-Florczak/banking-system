package banking.system.product;

import java.util.Set;

public interface CreditService {
    Credit findById(Long id);
    Set<Credit> findAll();
    Credit findByAccount(Long id);
    Credit createTransaction(CreditDTO creditDTO);
    void deleteOneById(Long id);
}
