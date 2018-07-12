package banking.system.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository addressRepository;

    @Test
    public void givenProperAddress_WhenSavedAndRetrievedFromDb_ThenSavingAndSavedCityAreEquals() {
        //given
        Address address = new Address("Poland",
                "Wrocław", "50-950", "Dąbrowskiego", "10");
        AddressCreateDTO dto = new AddressCreateDTO(address);
        AddressServiceImpl service = new AddressServiceImpl(addressRepository);

        //when
        Address saved = service.createAddress(dto);

        //then
        Assert.assertEquals(saved.getCity(), address.getCity());


    }
}