package ffeks.smykov_rv.dentistclinic.security.service.impl;

import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import ffeks.smykov_rv.dentistclinic.security.repository.UserAccountRepository;
import ffeks.smykov_rv.dentistclinic.security.service.UserAccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void createUserAccount(UserAccount userAccount) {

        boolean isExistsByPhoneNumber = userAccountRepository.existsByPhoneNumber(userAccount.getPhoneNumber());
        if (isExistsByPhoneNumber) {
            throw new RuntimeException("Account with this phone number already exists");
        }

        userAccountRepository.save(userAccount);
    }

//    @Override
//    public Optional<UserAccount> getUserByEmail(String email) {
//        return userAccountRepository.findByEmail(email);
//    }

    @Override
    public Optional<UserAccount> getUserByPhone(String phoneNumber) {
        return userAccountRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UserAccount getUserById(Long id) {
        if (userAccountRepository.existsById(id)) {
            return userAccountRepository.findById(id).get();
        }
        else  {
            throw new RuntimeException("Account with this phone id does not exist");
        }
    }

    @Override
    public boolean isExistById(Long id) {
        return userAccountRepository.findById(id).isPresent();
    }

    @Override
    public Boolean isExistByPhone(String phone) {
        Optional<UserAccount> userAccount = userAccountRepository.findByPhoneNumber(phone);
        if (userAccount.isPresent()) {
            return true;
        }
        else  {
            return false;
        }

    }

    @Override
    public Optional<UserAccount> getUserAccountDtoByToken() {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userAccountRepository.findByPhoneNumber(currentUsername);

    }
}
