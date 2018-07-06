package banking.system.exchangerate;

import banking.system.common.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private ExchangeRateRepository repository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExchangeRate findLast(Currency from, Currency to) {
        return repository.findFirstByFromCurrencyAndToCurrencyOrderByCreatedAtDesc(from, to);
    }

    @Override
    public ExchangeRate createCurrent(Currency from, Currency to) {
        CurrencyApi api = new CurrencyApi();
        ExchangeRate rate = new ExchangeRate();
        try {
            rate.setRate(api.getRatio(from.name(), to.name()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        rate.setToCurrency(to);
        rate.setFromCurrency(from);
        return repository.save(rate);
    }

    @Override
    public void updateRates() {
        for(Currency from : Currency.values()){
            for(Currency to: Currency.values()){
                if(!from.equals(to)){
                    createCurrent(from,to);
                }
            }
        }
    }
}