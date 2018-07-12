package banking.system.common.profits;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfitsRepository extends JpaRepository<Profits, Long> {
    Optional<Profits> findByName(String name);
}
