package banking.system.client;


import banking.system.security.token.VerificationToken;

import java.util.Set;

public interface ClientService {

    Client findById(Long id);

    Set<Client> findAll();

    void createVerificationToken(Client client);

    Client createAddress(ClientCreateDTO clientCreateDTO);

    void deleteOneById(Long id);

    VerificationToken getVerificationToken(String token);

    void saveRegisteredClient(Client client);

    Client findClientByEmail(String email);

    Client saveClient(ClientCreateDTO clientDTO);

    Client findByToken(String token);

}
