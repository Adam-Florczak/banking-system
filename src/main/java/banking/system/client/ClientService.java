package banking.system.client;


import banking.system.registration.VerificationToken;

import java.util.Set;

public interface ClientService {

    Client findById(Long id);

    Set<Client> findAll();

    void createVerificationToken(Client client, String token);

    Client registerNewUserAccount(ClientCreateDTO clientCreateDTO);

    Client createAddress(ClientCreateDTO clientCreateDTO);

    void deleteOneById(Long id);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredClient(Client client);
}
