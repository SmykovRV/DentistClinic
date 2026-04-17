package ffeks.smykov_rv.dentistclinic.security.repository;

import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserAccount> findByPhoneNumber(String phoneNumber);

}
