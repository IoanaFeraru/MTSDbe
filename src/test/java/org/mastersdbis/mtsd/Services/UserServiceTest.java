package org.mastersdbis.mtsd.Services;

import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;
import org.mastersdbis.mtsd.Entities.User.Role;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.Assertions;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void addUser() {
        User newUser = new User();
        newUser.setUsername("testASDF");
        newUser.setPassword("Parola11!");
        newUser.setEmail("stefan101@gmail.com");
        newUser.setPhoneNumber("+40 789678567");
        newUser.setAddress("Strada Exemplu, Nr. 10");

        userService.addUser(newUser);

        User savedUser = userService.findByUsername("Stefan");
        System.out.println("Detalii utilizator salvat: \n" + newUser);

        String encodedPassword = savedUser.getPassword();

        boolean passwordMatches = passwordEncoder.matches("Parola11!", encodedPassword);
        assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
    }

    @Test
    void updateUser() {
        User userFromDb = userService.findByUsername("Stefan");
        Assertions.assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        String newAddress = "Strada Exemplu";
        userFromDb.setAddress(newAddress);
        userService.updateUser(userFromDb);
        User updatedUser = userService.findByUsername("Stefan");
        Assertions.assertEquals(newAddress, updatedUser.getAddress(), "Adresa nu a fost actualizata corect.");
        System.out.println("Adresa a fost actualizata cu succes.");
    }

    @Test
    void updatePassword() {
        User userFromDb = userService.findByUsername("Stefan");
        Assertions.assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        String newPassword = "ParolaTare123!";
        userService.updateUserPassword(userFromDb, newPassword);
        User updatedUser = userService.findByUsername("Stefan");
        boolean passwordMatches = passwordEncoder.matches(newPassword, updatedUser.getPassword());
        Assertions.assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
        System.out.println("Parola a fost modificată cu succes!");
    }

    @Test
    void deleteUser() {
        User userFromDb = userService.findByUsername("Stefan");
        assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        userService.deleteUser(userFromDb);
        User deletedUser = userService.findByUsername("Stefan");
        Assertions.assertNull(deletedUser, "Utilizatorul nu a fost sters din baza de date.");
        System.out.println("Utilizatorul a fost sters cu succes.");
    }

    @Test
    void addProvider() {
        User testUser = new User();
        testUser.setUsername("Ioana");
        testUser.setPassword("Parola11!");
        testUser.setEmail("ioana@gmail.com");
        testUser.setPhoneNumber("+40 789678567");
        testUser.setAddress("Strada Exemplu, Nr. 10");
        userService.addUser(testUser);
        testUser = userService.findByUsername("Ioana");

        Provider testProvider = new Provider();
        testProvider.setUser(testUser);
        testProvider.setCif("RO1234567890");
        testProvider.setCompanyName("Test Company");
        testProvider.setCompanyAdress("Strada Test, Nr. 1");
        testProvider.setServiceDomain(ServiceDomain.INFORMATICE);
        testProvider.setBankIBAN("RO49AAAA1B31007593840000");
        userService.addProvider(testProvider);

        Provider retrievedProvider = userService.findProviderByUser(testUser);

        assertNotNull(retrievedProvider, "Provider-ul ar trebui să fie găsit.");
        System.out.println("Detalii utilizator salvat: \n" + retrievedProvider);
    }

    @Test
    void validateProvider() {
        User adminUser = new User();
        adminUser.setUsername("Admin");
        adminUser.setPassword("Parola11!");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setPhoneNumber("+40 789678567");
        userService.addUser(adminUser);
        adminService.makeAdmin(adminUser);

        User providerUser = userService.findByUsername("Ioana");
        assertNotNull(providerUser, "Utilizatorul Ioana ar trebui să existe.");

        Provider provider = userService.findProviderByUser(providerUser);
        assertNotNull(provider, "Provider-ul asociat utilizatorului Ioana ar trebui să fie găsit.");
        assertEquals(ValidationStatus.PENDING, provider.getValidationStatus(),
                "Statusul inițial al provider-ului ar trebui să fie PENDING.");

        adminService.validateProvider(provider, adminUser);

        Provider updatedProvider = userService.findProviderByUser(providerUser);
        assertNotNull(updatedProvider, "Provider-ul ar trebui să fie găsit după validare.");
        assertEquals(ValidationStatus.APPROVED, updatedProvider.getValidationStatus(),
                "Statusul provider-ului ar trebui să fie APPROVED după validare.");

        User updatedProviderUser = userService.findByUsername("Ioana");
        assertNotNull(updatedProviderUser, "Utilizatorul Ioana ar trebui să existe după actualizare.");

        assertTrue(updatedProviderUser.getRoles().contains(Role.PROVIDER),
                "Utilizatorul Ioana ar trebui să aibă rolul PROVIDER după validare.");
    }

}
