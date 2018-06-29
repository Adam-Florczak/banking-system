package banking.system.client;


import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;

public interface ClientService extends UserDetailsService {
    Client findById(Long id);

    Set<Client> findAll();

    void createVerificationToken(Client client, String token);

    Client registerNewUserAccount(ClientCreateDTO clientCreateDTO);

    Client findByEmail(String email);

    void deleteOneById(Long id);

    Client createAddress(ClientCreateDTO clientCreateDTO);
}
