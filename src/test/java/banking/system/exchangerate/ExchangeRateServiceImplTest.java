package banking.system.exchangerate;

import banking.system.common.Currency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateServiceImplTest {

    @Autowired
    private ExchangeRateRepository repository;

    @Test
    public void givenExchangeRates_WhenSavedAndRetrievedFromDb_ThenAllCurrenciesSaved() {

        ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(repository);
        service.updateRates();

        for (Currency from : Currency.values()) {
            for (Currency to : Currency.values()) {
                if (!from.equals(to)) {
                    ExchangeRate rate = repository.findFirstByFromCurrencyAndToCurrencyOrderByCreatedAtDesc(from, to);

                    Assert.assertEquals(from, rate.getFromCurrency());
                    Assert.assertEquals(to, rate.getToCurrency());
                    Assert.assertNotNull(rate.getRate());

                }
            }
        }
    }


    @Test
    public void givenSomeObject_WhenRetrievingTheLatest_ThenGotTheLatest() {

        ExchangeRate rate1 = new ExchangeRate();
        rate1.setCreatedAt(LocalDateTime.MIN);
        rate1.setFromCurrency(Currency.PLN);
        rate1.setToCurrency(Currency.USD);
        repository.save(rate1);

        ExchangeRate rate2 = new ExchangeRate();
        rate2.setCreatedAt(LocalDateTime.now());
        rate2.setFromCurrency(Currency.PLN);
        rate2.setToCurrency(Currency.USD);
        repository.save(rate2);

        ExchangeRate rate3 = new ExchangeRate();
        rate3.setCreatedAt(LocalDateTime.MAX);
        rate3.setFromCurrency(Currency.PLN);
        rate3.setToCurrency(Currency.USD);
        rate3 = repository.save(rate3);
        rate3.setCreatedAt(LocalDateTime.MAX);
        repository.save(rate3);

        ExchangeRate saved = repository.findFirstByFromCurrencyAndToCurrencyOrderByCreatedAtDesc(Currency.PLN, Currency.USD);
        Assert.assertEquals(LocalDateTime.MAX, saved.getCreatedAt());


    }
}
