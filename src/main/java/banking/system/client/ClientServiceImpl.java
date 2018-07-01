package banking.system.client;


import banking.system.registration.VerificationToken;
import banking.system.registration.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private AddressRepository addressRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository,
                             PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

    @Override
    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Client saveClient(ClientCreateDTO clientDTO) {
        Client client = new Client();

        client.setEmail(clientDTO.getEmail());
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        client.setAddress(addressRepository.findById(clientDTO.getAddressId()).orElse(null));
        Role role = roleRepository.findByRole("ADMIN");
        client.setRoles(new HashSet<Role>(Arrays.asList(role)));
      // TODO client veryfication through e-mail
        client.setEnabled(true);

        return clientRepository.save(client);
    }
}
