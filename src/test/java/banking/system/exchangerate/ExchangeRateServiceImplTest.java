//package banking.system.exchangerate;
//
//import banking.system.App;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//
//public class ExchangeRateServiceImplTest {
//
//    @Autowired
//    private ExchangeRateRepository repository;
//
//    @Test
//    public void givenExchangeRates_WhenSavedAndRetrievedFromDb_ThenAllCurrenciesSaved() {
//
//        ExchangeRateServiceImpl service = new ExchangeRateServiceImpl(repository);
//
//        ExchangeRate saved = service.createCurrent();
//
//        Assert.assertNotNull(saved.getChf());
//        Assert.assertNotNull(saved.getGbp());
//        Assert.assertNotNull(saved.getEur());
//        Assert.assertNotNull(saved.getUsd());
//    }
//
//    @Test
//    public void givenSomeObject_WhenRetrievingTheLatest_ThenGotTheLatest() {
//
//        repository.save(new ExchangeRate());
//        repository.save(new ExchangeRate());
//        ExchangeRate obj1 = repository.save(new ExchangeRate());
//        obj1.setCreatedAt(LocalDateTime.MAX);
//        repository.save(obj1);
//        ExchangeRate saved = repository.findOneByCreatedAtOrderByCreatedAtDesc();
//        Assert.assertEquals(saved.getCreatedAt(), LocalDateTime.MAX);
//
//
//    }
//}