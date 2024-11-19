package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.User.Admin.Admin;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.AdminRepository;
import org.mastersdbis.mtsd.Repositories.ProviderRepository;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    private final ProviderRepository providerRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository, ProviderRepository providerRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

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

    public void saveAdmin(Admin admin) {
        User managedUser = userRepository.findById(admin.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există"));
        admin.setUser(managedUser);
        adminRepository.save(admin);
    }

    public Admin findByUser(User user) {
        return adminRepository.findByUser(user);
    }
    //TODO - daca vor mai face adminii ceva ??
}
