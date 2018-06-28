package banking.system.client;


import java.util.Set;

public interface ClientService {
    Client findById(Long id);

    Set<Client> findAll();

    Client createAddress(ClientCreateDTO address);

    void deleteOneById(Long id);

    void createVerificationToken(Client client, String token);
}
