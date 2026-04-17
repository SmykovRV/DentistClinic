package ffeks.smykov_rv.dentistclinic.security.usecase;

import ffeks.smykov_rv.dentistclinic.security.web.model.RegisterRequest;

public interface RegisterUserAccountUseCase {
    void registerUserAccount(RegisterRequest registerRequest);
}
