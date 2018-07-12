package banking.system.client;


import banking.system.account.Account;
import banking.system.account.AccountService;
import banking.system.account.AccountServiceImpl;
import banking.system.account.AccountType;
import banking.system.common.Currency;
import banking.system.security.token.VerificationToken;
import banking.system.security.token.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    private VerificationTokenRepository tokenRepository;
    private AccountService accountService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder, JavaMailSender mailSender, VerificationTokenRepository tokenRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.tokenRepository = tokenRepository;
        this.accountService = accountService;
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
    public void createVerificationToken(Client client) {
        String token = client.getUuid();
        VerificationToken verificationToken = new VerificationToken(token, client);
        tokenRepository.save(verificationToken);
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
        Client client = createClientFromDTO(clientDTO);

        clientRepository.save(client);
        Client client2 = clientRepository.findByEmail(client.getEmail()).orElseThrow(RuntimeException::new);
        createVerificationToken(client2);

        sendVerificationEmail(client2);
        return client;
    }

    private Client createClientFromDTO(ClientCreateDTO clientDTO) {
        Client client = new Client();

        client.setEmail(clientDTO.getEmail());
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));

        Address address = new Address();

        address.setCountry(clientDTO.getCountry());
        address.setCity(clientDTO.getCity());
        address.setStreet(clientDTO.getStreet());
        address.setZipCode(clientDTO.getZipCode());
        address.setNumber(clientDTO.getNumber());

        client.setAddress(address);
        clientRepository.save(client);

        Account account = new Account();

        String accNumber = accountService.generateAccountNumber();
        account.setNumber(accNumber);
        account.setOwner(client);
        account.setCurrency(Currency.PLN);
        account.setBalance(BigDecimal.ZERO);
        account.setInterest(BigDecimal.ZERO);
        account.setProvision(BigDecimal.ZERO);
        account.setType(AccountType.PERSONAL);

        Set<Account> accounts = new HashSet<>();
        accounts.add(account);
        client.setAccountSet(accounts);

        return clientRepository.save(client);
    }

    private void sendVerificationEmail(Client client) {
        String email = client.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "http://localhost:8080/registerConfirm?token=" + client.getUuid();

        sendMail(email, subject, confirmationUrl);
    }

    private void sendMail(String email, String subject, String text) {
        SimpleMailMessage sendMail = new SimpleMailMessage();
        sendMail.setTo(email);
        sendMail.setSubject(subject);
        sendMail.setText(text);
        mailSender.send(sendMail);
    }

    @Override
    public Client findByToken(String token) {
        return tokenRepository.findByToken(token).getClient();
    }
}