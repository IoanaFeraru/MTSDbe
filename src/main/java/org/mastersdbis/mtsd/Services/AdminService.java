package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Entities.User.Role;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.ProviderRepository;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class AdminService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(ProviderRepository providerRepository, UserRepository userRepository) {
        this.providerRepository = providerRepository;
        this.userRepository = userRepository;
    }

    public void validateProvider(Provider provider, User adminUser) {
        if (!provider.getValidationStatus().equals(ValidationStatus.PENDING)) {
            throw new IllegalArgumentException("Provider can only be validated if status is PENDING.");
        }

        User user = userRepository.findById(provider.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + provider.getId()));

        provider.setValidationStatus(ValidationStatus.APPROVED);
        provider.setAprovedBy(adminUser);

        Set<Role> updatedRoles = new HashSet<>(user.getRoles());
        updatedRoles.add(Role.PROVIDER);
        user.setRoles(updatedRoles);

        userRepository.saveAndFlush(user);
        providerRepository.save(provider);
    }

    public void denyProvider(Provider provider, User adminUser) {
        if (!provider.getValidationStatus().equals(ValidationStatus.PENDING)) {
            throw new IllegalArgumentException("Provider can only be denied if status is PENDING.");
        }

        provider.setValidationStatus(ValidationStatus.DENIED);
        providerRepository.save(provider);
    }

    public void makeAdmin(User user) {
        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.add(Role.ADMIN);
        roles.add(Role.PROVIDER);
        user.setRoles(roles);

        userRepository.save(user);
    }

}
