package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository providerRepository;

    public void validateProvider(Provider provider, boolean isApproved) {
        if (!provider.getValidationStatus().equals(ValidationStatus.PENDING)) {
            throw new IllegalArgumentException("Provider can only be validated if status is PENDING.");
        }

        if (isApproved) {
            provider.setValidationStatus(ValidationStatus.APPROVED);
        } else {
            provider.setValidationStatus(ValidationStatus.DENIED);
        }

        providerRepository.save(provider);
    }

    //TODO - daca vor mai face adminii ceva ??
}
