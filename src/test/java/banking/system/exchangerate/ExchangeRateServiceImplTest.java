
package banking.system.exchangerate;

import banking.system.App;
import banking.system.common.Currency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateServiceImplTest {

    @Autowired
    private ExchangeRateRepository repository;

    @Test
    public void givenExchangeRates_WhenSavedAndRetrievedFromDb_ThenAllCurrenciesSaved() {

        ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(repository);
        service.updateRates();
        for(Currency from : Currency.values()){
            for(Currency to: Currency.values()){
                if(!from.equals(to)){
                    Assert.assertNotNull(repository.findFirstByFromAndToOrderByCreatedAtDesc(from,to));
                }
            }
        }
    }

    //todo
//    @Test
//    public void givenSomeObject_WhenRetrievingTheLatest_ThenGotTheLatest() {
//
//        repository.save(new ExchangeRate());
//        repository.save(new ExchangeRate());
//        ExchangeRate obj1 = repository.save(new ExchangeRate());
//        obj1.setCreatedAt(LocalDateTime.MAX);
//        repository.save(obj1);
//        ExchangeRate saved = repository.findFirstByOrderByCreatedAtDesc();
//        Assert.assertEquals(saved.getCreatedAt(), LocalDateTime.MAX);
//
//
//    }
}
