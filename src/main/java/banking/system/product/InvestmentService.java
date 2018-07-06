package banking.system.product;

import java.util.Set;
public interface InvestmentService {

    Investment findById(Long id);
        Set<Investment> findAll();
        Investment findByAccount(Long id);
        Investment createInvestment(InvestmentDTO investmentDTO);
        void deleteOneById(Long id);
}
