package ffeks.smykov_rv.dentistclinic.security.usecase.impl;

import ffeks.smykov_rv.dentistclinic.reservation.dto.UserAccountMapper;
import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.UserAccountDto;
import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import ffeks.smykov_rv.dentistclinic.security.service.UserAccountService;
import ffeks.smykov_rv.dentistclinic.security.usecase.GetUserAccountInfoUseCase;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GetUserAccountInfoFacade implements GetUserAccountInfoUseCase {

    private final UserAccountMapper userAccountMapper;
    private final UserAccountService userAccountService;

    public GetUserAccountInfoFacade(UserAccountMapper userAccountMapper, UserAccountService userAccountService) {
        this.userAccountMapper = userAccountMapper;
        this.userAccountService = userAccountService;
    }

    @Override
    public UserAccountDto getUserAccountInfo() {
        Optional<UserAccount> userAccount = userAccountService.getUserAccountDtoByToken();
        if (userAccount.isPresent()) {
            return userAccountMapper.toUserAccountDto(userAccount.get());
        }
        else {
            throw new RuntimeException("UserAccount not found");
        }
    }
}
