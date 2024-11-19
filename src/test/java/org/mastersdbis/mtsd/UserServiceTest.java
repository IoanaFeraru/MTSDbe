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

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void addUser() {
        User newUser = new User();
        newUser.setUsername("Ioana");
        newUser.setPassword("Parola11!");
        newUser.setEmail("stefan@gmail.com");
        newUser.setPhoneNumber("+40 789678567");
        newUser.setAddress("Strada Exemplu, Nr. 10");

        userService.addUser(newUser);

        User savedUser = userService.findByUsername("Ioana");
        System.out.println("Detalii utilizator salvat: \n" + savedUser.toString());

        String encodedPassword = savedUser.getPassword();

        boolean passwordMatches = passwordEncoder.matches("Parola11!", encodedPassword);
        Assertions.assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
    }

    @Test
    void updateUser() {
        User userFromDb = userService.findByUsername("Ioana");
        Assertions.assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        String newUsername = "Stefan";
        userFromDb.setUsername(newUsername);
        userService.updateUser(userFromDb);
        User updatedUser = userService.findByUsername(newUsername);
        Assertions.assertEquals(newUsername, updatedUser.getUsername(), "Username-ul nu a fost actualizat corect.");
        System.out.println("Username-ul a fost actualizat cu succes.");
    }
    @Test
    void updatePassword() {
        // Retrieve user from database
        User userFromDb = userService.findByUsername("Ioana");
        Assertions.assertNotNull(userFromDb, "Utilizatorul cu username-ul 'Ioana' nu a fost găsit în baza de date.");
        String newPassword = "ParolaTare123!";
        userFromDb.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(userFromDb);
        User updatedUser = userService.findByUsername("Ioana");
        boolean passwordMatches = passwordEncoder.matches(newPassword, updatedUser.getPassword());
        Assertions.assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
        System.out.println("Parola a fost modificată cu succes!");
    }

    @Test
    void deleteUser() {
        User userFromDb = userService.findByUsername("Ioana");
        Assertions.assertNotNull(userFromDb, "Utilizatorul nu a fost găsit în baza de date.");
        userService.deleteUser(userFromDb);
        User deletedUser = userService.findByUsername("Ioana");
        Assertions.assertNull(deletedUser, "Utilizatorul nu a fost sters din baza de date.");
        System.out.println("Utilizatorul a fost sters cu succes.");
    }

    @Test
    void testMakeProvider() {
        User user = userService.findByUsername("Ioana");

        Provider provider = new Provider();
        provider.setUser(user);
        provider.setCif("RO1234567890");
        provider.setCompanyName("Exemplu SRL");
        provider.setCompanyAdress("Strada Firma, Nr. 1");
        provider.setServiceDomain(ServiceDomain.INFORMATICE);
        provider.setBankIBAN("RO49AAAA1B31007593840000");

        userService.addProvider(provider);

    }

}
