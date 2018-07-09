package banking.system.security;

import banking.system.client.Client;
import banking.system.client.ClientRepository;
import banking.system.exceptions.ClientNotActivatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<Client> byEmail = clientRepository.findByEmail(s);
        if(!byEmail.isPresent()){
            throw new UsernameNotFoundException(s);
        }
        Client client = byEmail.get();

        if(client.isEnabled())
            return new ClientUserDetails(client);
        else
            throw new ClientNotActivatedException();
    }
}
