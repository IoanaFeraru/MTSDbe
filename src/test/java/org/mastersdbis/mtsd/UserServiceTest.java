package org.mastersdbis.mtsd;

import org.junit.jupiter.api.Test;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        User newUser = new User();
        newUser.setUsername("Stefan");
        newUser.setPassword("Parola11!");
        newUser.setEmail("stefan@gmail.com");
        newUser.setPhoneNumber("+40 789678567");
        newUser.setAddress("Strada Exemplu, Nr. 10");

        userService.addUser(newUser);

        User savedUser = userService.findByUsername("Stefan");
        System.out.println("Detalii utilizator salvat: \n" + savedUser.toString());

        String encodedPassword = savedUser.getPassword();

        boolean passwordMatches = passwordEncoder.matches("Parola11!", encodedPassword);
        Assertions.assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
    }

    @Test
    void updateUser() {
        User userFromDb = userService.findByUsername("Stefan");
        assertNotNull(userFromDb, "Utilizatorul cu username-ul 'Ioana' nu a fost găsit în baza de date.");
        assertEquals("Stefan", userFromDb.getUsername(), "Username-ul curent nu corespunde celui așteptat.");
        String newUsername = "Ioana";
        userFromDb.setUsername(newUsername);
        userService.updateUser(userFromDb);
        User updatedUser = userService.findByUsername(newUsername);
        assertNotNull(updatedUser, "Utilizatorul cu noul username 'IoanaUpdated' nu a fost găsit în baza de date.");
        assertEquals(newUsername, updatedUser.getUsername(), "Username-ul nu a fost actualizat corect.");
        User oldUser = userService.findByUsername("Stefan");
        Assertions.assertNull(oldUser, "Utilizatorul cu vechiul username 'Ioana' există încă în baza de date.");
        System.out.println("Username-ul a fost actualizat cu succes.");
    }

    @Test
    void deleteUser() {
        User userFromDb = userService.findByUsername("Ioana");
        assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        userService.deleteUser(userFromDb);
        User deletedUser = userService.findByUsername("Ioana");
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

        // Creează un provider asociat
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
        System.out.println("Detalii utilizator salvat: \n" + retrievedProvider.toString());
    }


}
