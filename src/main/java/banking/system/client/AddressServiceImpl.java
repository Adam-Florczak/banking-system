package banking.system.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService{

    private AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address findById(Long id) {
        Optional<Address> optionalAddress=addressRepository.findById(id);
        if(optionalAddress.isPresent()){
            return optionalAddress.get();
        }
        else{
            throw new RuntimeException("No address found");
        }
    }

    @Override
    public Set<Address> findAll() {
        return new HashSet<>(addressRepository.findAll());
    }

    @Override
    public Address createAddress(AddressCreateDTO addressCreateDTO) {
        Address address = new Address();
        address.setCountry(addressCreateDTO.getCountry());
        address.setCity(addressCreateDTO.getCity());
        address.setStreet(addressCreateDTO.getStreet());
        address.setNumber(addressCreateDTO.getNumber());
        address.setZipCode(addressCreateDTO.getZipCode());
        return addressRepository.save(address);
    }

    @Override
    public void deleteOneById(Long id) {
        addressRepository.delete(id);
    }
}
