package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.mastersdbis.mtsd.DTO.ProviderDTO;
import org.mastersdbis.mtsd.DTO.UserUpdateDTO;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User existingUser = userService.findByUsername(username);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            if (userUpdateDTO.getUsername() != null) {
                existingUser.setUsername(userUpdateDTO.getUsername());
            }
            if (userUpdateDTO.getEmail() != null) {
                existingUser.setEmail(userUpdateDTO.getEmail());
            }
            if (userUpdateDTO.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(userUpdateDTO.getPhoneNumber());
            }
            if (userUpdateDTO.getAddress() != null) {
                existingUser.setAddress(userUpdateDTO.getAddress());
            }

            userService.updateUser(existingUser);

            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<String> updateUserPassword(@PathVariable String username, @RequestBody PasswordRequest passwordRequest) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            userService.updateUserPassword(user, passwordRequest.getPassword());
            return ResponseEntity.ok("Password updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid password: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating password: " + e.getMessage());
        }
    }

    @Setter
    @Getter
    public static class PasswordRequest {
        private String password;
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByUsernamePattern(@RequestParam String usernamePattern) {
        if (usernamePattern.length() < 3) {
            return ResponseEntity.badRequest().body("The search pattern must contain at least 3 characters.");
        }
        try {
            List<User> users = userService.searchByUsernamePattern(usernamePattern);
            if (users.isEmpty()) {
                return ResponseEntity.ok("No users found matching the pattern.");
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving users: " + e.getMessage());
        }
    }

    @PostMapping("/addProvider")
    public ResponseEntity<String> addProvider(@RequestBody @Valid ProviderDTO providerDTO) {
        try {
            Provider provider = new Provider();
            provider.setUser(userService.findById(providerDTO.getUserId()));
            provider.setCif(providerDTO.getCif());
            provider.setCompanyName(providerDTO.getCompanyName());
            provider.setCompanyAdress(providerDTO.getCompanyAdress());
            provider.setServiceDomain(providerDTO.getServiceDomain());
            provider.setBankIBAN(providerDTO.getBankIBAN());

            userService.addProvider(provider);

            return ResponseEntity.ok("Provider added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding provider: " + e.getMessage());
        }
    }

    @GetMapping("/searchProvider/{username}")
    public ResponseEntity<Provider> findProvider(@PathVariable String username) {
        try {
            Provider provider = userService.findProviderByUser(userService.findByUsername(username));
            if (provider == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(provider);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
