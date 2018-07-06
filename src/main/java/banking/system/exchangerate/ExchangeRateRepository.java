package banking.system.exchangerate;

import banking.system.common.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {


  ExchangeRate findFirstByFromCurrencyAndToCurrencyOrderByCreatedAtDesc(Currency from, Currency to);

}