package org.mastersdbis.mtsd;

import org.junit.jupiter.api.Test;
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
        // Creează utilizatorul
        User newUser = new User();
        newUser.setUsername("Stefan");
        newUser.setPassword("Parola11!");
        newUser.setEmail("stefan@gmail.com");
        newUser.setPhoneNumber("+40 789678567");
        newUser.setAddress("Strada Exemplu, Nr. 10");

        // Salvează utilizatorul
        userService.addUser(newUser);

        User savedUser = userService.findByUsername("Stefan");
        System.out.println("Detalii utilizator salvat: " + savedUser.toString());

        String encodedPassword = savedUser.getPassword();

        boolean passwordMatches = passwordEncoder.matches("Parola11!", encodedPassword);

        Assertions.assertTrue(passwordMatches, "Parola nu a fost codificată corect.");
    }
}
