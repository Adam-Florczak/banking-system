package banking.system.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private AddressRepository addressRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        } else {
            throw new RuntimeException("No client found");
        }
    }

    @Override
    public Set<Client> findAll() {
        return new HashSet<>(clientRepository.findAll());
    }

    @Override
    public Client createAddress(ClientCreateDTO clientCreateDTO) {
        Client client = new Client();
        Optional<Address> optionalAddress = addressRepository.findById(clientCreateDTO.getAddressId());
        if (optionalAddress.isPresent()) {
            client.setAddress(optionalAddress.get());
        } else throw new RuntimeException("No address found");
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
    public Client registerNewUserAccount(ClientCreateDTO clientCreateDTO) {

        Client client = clientRepository.findByEmail(clientCreateDTO.getEmail()).orElseThrow(RuntimeException::new);

        client.setEmail(clientCreateDTO.getEmail());
        client.setFirstName(clientCreateDTO.getFirstName());
        client.setLastName(clientCreateDTO.getLastName());
        client.setAddress(addressRepository.findById(clientCreateDTO.getAddressId()).orElse(null));
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        client.setEnabled(true);

        return client;
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Client client = clientRepository.findByEmail(email).orElse(null);
        if (client == null) {
            throw new UsernameNotFoundException(email);
        }
        return new MyClientPrincipal(client);
    }
}
