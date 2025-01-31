package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Entities.User.Role;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.ProviderRepository;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProviderRepository providerRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ProviderRepository providerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.providerRepository = providerRepository;
    }

    public void addUser(User user) {
        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        userRepository.save(user);
    }

    public void updateProvider(Provider provider) {
        User managedUser = userRepository.findById(provider.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există"));
        provider.setUser(managedUser);
        providerRepository.save(provider);
    }
    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updateUserPassword(User user, String password) {
        validatePassword(password);
        userRepository.updateUserPassword(user.getId(), passwordEncoder.encode(password));
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public Provider findProviderByUser(User user) {
        return providerRepository.findByUser(user);
    }

    public Provider findById(Integer id) {return providerRepository.findById(id).orElse(null);}

    public List<Provider> findAllProviders() {return providerRepository.findAll();}

    public User findByUsername(String username) {return userRepository.findByUsername(username);}

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void addProvider(Provider provider) {
        User managedUser = userRepository.findById(provider.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu există"));
        provider.setUser(managedUser);

        provider.setValidationStatus(ValidationStatus.PENDING);
        providerRepository.save(provider);
    }

    public List<User> searchByUsernamePattern(String pattern) {
        return userRepository.searchByUsernamePattern("%" + pattern.toLowerCase() + "%");
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Parola trebuie să aibă cel puțin 8 caractere.");
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new IllegalArgumentException("Parola trebuie să conțină cel puțin o literă mare.");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new IllegalArgumentException("Parola trebuie să conțină cel puțin o cifră.");
        }
        if (!Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
            throw new IllegalArgumentException("Parola trebuie să conțină cel puțin un caracter special.");
        }
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

}
