package banking.system.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    Optional<Investment> findById(Long id);
    Set<Investment> findAllByAccount_Number(String accountNumber);
}

