package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.DTO.UserUpdateDTO;
import org.mastersdbis.mtsd.Entities.User.User;
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

    public static class PasswordRequest {
        private String password;
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
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
}
