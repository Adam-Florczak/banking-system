package banking.system.client;


import banking.system.registration.VerificationToken;
import banking.system.registration.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private AddressRepository addressRepository;
    private VerificationTokenRepository tokenRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> optionalClient= clientRepository.findById(id);
        if(optionalClient.isPresent()){
            return optionalClient.get();
        }
        else{
            throw new RuntimeException("No client found");
        }
    }

    @Override
    public Set<Client> findAll() {
        return new HashSet<>(clientRepository.findAll());
    }

    @Override
    public void createVerificationToken(Client client, String token) {

    }

    @Override
    public Client registerNewUserAccount(ClientCreateDTO clientCreateDTO) {
        return null;
    }

   @Override
    public Client createAddress(ClientCreateDTO clientCreateDTO) {
        Client client=new Client();
        Optional<Address> optionalAddress=addressRepository.findById(clientCreateDTO.getAddressId());
        if(optionalAddress.isPresent()){
            client.setAddress(optionalAddress.get());
        }
        else throw new RuntimeException("No address found");
        client.setEmail(clientCreateDTO.getEmail());
        client.setFirstName(clientCreateDTO.getFirstName());
        client.setLastName(clientCreateDTO.getLastName());
        client.setPassword(clientCreateDTO.getPassword());
        return clientRepository.save(client);
    }

    @Override
    public void deleteOneById(Long id) {
        clientRepository.delete(id);
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredClient(Client client) {
        clientRepository.save(client);
    }

}
