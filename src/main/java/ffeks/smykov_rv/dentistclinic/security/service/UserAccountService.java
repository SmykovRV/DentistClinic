package ffeks.smykov_rv.dentistclinic.security.service;

import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;

import java.util.Optional;

public interface UserAccountService {
    void createUserAccount(UserAccount userAccount);

//    Optional<UserAccount> getUserByEmail(String email);

    Optional<UserAccount> getUserByPhone(String phoneNumber);

    UserAccount getUserById(Long id);

    boolean isExistById(Long id);

    Boolean isExistByPhone(String phone);

    Optional<UserAccount> getUserAccountDtoByToken();

}
