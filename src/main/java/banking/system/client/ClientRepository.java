package banking.system.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findClientByUuid(String uuid);
    Optional<Client> findByEmail(String email);
    Optional<Client> findById(Long id);
}
