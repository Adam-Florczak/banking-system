package banking.system.exchangerate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final BigDecimal RATE = new BigDecimal("0.05");
    private ExchangeRateRepository repository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExchangeRate findLast() {
        return repository.findOneByCreatedAtOrderByCreatedAtDesc();
    }

    @Override
    public ExchangeRate createCurrent() {
        CurrencyApi api = new CurrencyApi();
        ExchangeRate rate = new ExchangeRate();

        try {
            rate.setEur(api.getRatio("EUR"));
            rate.setGbp(api.getRatio("GBP"));
            rate.setUsd(api.getRatio("USD"));
            rate.setChf(api.getRatio("CHF"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return repository.save(rate);


    }



}
