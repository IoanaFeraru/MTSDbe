package org.mastersdbis.mtsd.Services;

import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(User user) {
        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
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

    public List<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void makeProvider(User user, String cif, String companyName, String companyAddress, String serviceDomain, String bankIBAN) {
        if (user instanceof Provider) {
            throw new IllegalArgumentException("User is already a provider.");
        }

        Provider provider = new Provider();

        provider.setId(user.getId());
        provider.setUsername(user.getUsername());
        provider.setPassword(user.getPassword());
        provider.setEmail(user.getEmail());
        provider.setPhoneNumber(user.getPhoneNumber());
        provider.setAddress(user.getAddress());
        provider.setRating(user.getRating());
        provider.setNotificationPreferences(user.getNotificationPreferences());

        provider.setCif(cif);
        provider.setCompanyName(companyName);
        provider.setCompanyAdress(companyAddress);
        provider.setServiceDomain(ServiceDomain.valueOf(serviceDomain));
        provider.setBankIBAN(bankIBAN);

        provider.setValidationStatus(ValidationStatus.PENDING);

        userRepository.save(provider);
    }

    public List<User> searchByUsernamePattern(String pattern) {
        return userRepository.searchByUsernamePattern(pattern);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            throw new IllegalArgumentException("Password must contain at least one digit.");
        }
    }

    public boolean authenticateUser(String username, String rawPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username).stream().findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return true;
            } else {
                throw new IllegalArgumentException("Parola incorectă.");
            }
        } else {
            throw new IllegalArgumentException("Utilizatorul nu există.");
        }
    }

    //TODO implementare exceptii
    //TODO autentificare (verifica daca hash la parola primita = parola hashed din bd si returneaza un bool)
    //TODO calcul rating profil (nu se salveaza in bd)
    //TODO future - statistici provider (prin query uri, o sa fie probabil cand ne apucam de frontend)
}
