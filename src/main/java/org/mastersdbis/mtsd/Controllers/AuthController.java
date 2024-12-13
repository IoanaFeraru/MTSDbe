package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import org.mastersdbis.mtsd.DTO.UserDTO;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }

    @GetMapping("/provider/home")
    public String providerHome() {
        return "provider/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Formularul conține erori");
        }

        if (userService.findByUsername(userDTO.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Acest nume de utilizator este deja folosit.");
        }

        if (userService.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Această adresă de email este deja folosită.");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userService.addUser(user);

        return ResponseEntity.ok("Utilizator înregistrat cu succes");
    }

    //Update user (fara parola, aici nu se pune parola)
    //Update user password
    //creare provider
    //validare provider
    //update provider
    //un get care sa arate userul
    //get provider
    //get searchByUsernamePattern
    //mapare pentru logare?
}
