package banking.system.exchangerate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;


public class CurrencyApiTest {

    @Test
    public void getRatio() throws IOException {
        CurrencyApi api = new CurrencyApi();
        Assert.assertTrue(new BigDecimal("0.26561").equals(api.getRatio("PLN", "USD")));


    }
}