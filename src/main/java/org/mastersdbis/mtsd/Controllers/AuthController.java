package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import org.mastersdbis.mtsd.DTO.UserDTO;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO) {
        if (userService.findByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Acest nume de utilizator este deja folosit.");
        }

        if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Această adresă de email este deja folosită.");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userService.addUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Utilizator înregistrat cu succes");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        User user = userService.findByUsername(userDTO.getUsername());
        if (user == null || !user.getPassword().equals(userDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Autentificare eșuată: nume de utilizator sau parolă greșită");
        }

        return ResponseEntity.ok("Autentificare reușită");
    }

}
