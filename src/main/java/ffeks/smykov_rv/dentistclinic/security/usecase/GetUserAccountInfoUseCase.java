package ffeks.smykov_rv.dentistclinic.security.usecase;

import ffeks.smykov_rv.dentistclinic.reservation.dto.mapping.UserAccountDto;

public interface GetUserAccountInfoUseCase {
    UserAccountDto getUserAccountInfo();
}
