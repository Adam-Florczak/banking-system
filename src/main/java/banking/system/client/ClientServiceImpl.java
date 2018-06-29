package banking.system.client;

import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public void createVerificationToken(Client client, String token) {

    }

    @Override
    public Client registerNewUserAccount(ClientCreateDTO clientCreateDTO) {
        return null;
    }
}
