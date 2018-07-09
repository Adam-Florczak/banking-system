package banking.system.product;

import java.util.Set;

public interface CreditService {
    Credit findById(Long id);
    Set<Credit> findAll();
    Set<Credit> findByAccount(Long id);
    Credit createCredit(CreditDTO creditDTO);
    void deleteOneById(Long id);
}
