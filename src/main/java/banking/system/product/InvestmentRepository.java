package banking.system.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    Optional<Investment> findById(Long id);
    Optional<Investment> findByAccount_Number(String accountNumber);
}

