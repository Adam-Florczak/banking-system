package banking.system.client;


import banking.system.security.token.VerificationToken;
import banking.system.security.token.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    private VerificationTokenRepository tokenRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, VerificationTokenRepository tokenRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.tokenRepository = tokenRepository;
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
    public void createVerificationToken(Client client) {
        String token = client.getUuid();
        VerificationToken verificationToken = new VerificationToken(token, client);
        tokenRepository.save(verificationToken);
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

        clientRepository.save(client);
        Client client2 = clientRepository.findByEmail(client.getEmail()).orElseThrow(RuntimeException::new);
        createVerificationToken(client2);

        sendEmail(client2);
        return client;
    }

    private void sendEmail(Client client){
        String email = client.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:8080/registerConfirm?token=" + client.getUuid();

        SimpleMailMessage sendMail = new SimpleMailMessage();
        sendMail.setTo(email);
        sendMail.setSubject(subject);
        sendMail.setText(confirmationUrl);
        mailSender.send(sendMail);
    }

    @Override
    public Client findByToken(String token){
        return tokenRepository.findByToken(token).getClient();
    }


}
