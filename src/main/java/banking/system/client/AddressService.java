package banking.system.client;

import java.util.Optional;
import java.util.Set;

public interface AddressService {
    Address findById(Long id);

    Set<Address> findAll();

    Address createAddress(AddressCreateDTO address);

    void deleteOneById(Long id);
}
