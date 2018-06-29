package banking.system.client;

public interface ClientService {

    void createVerificationToken(Client client, String token);

    Client registerNewUserAccount(ClientCreateDTO clientCreateDTO);
}
