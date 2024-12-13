package org.mastersdbis.mtsd.Controllers;

import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Update an existing user's details.
     *
     * @param user The updated user object.
     * @return ResponseEntity indicating the result of the operation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            User existingUser = userService.findById(id);
            if (existingUser == null) {
                return ResponseEntity.notFound().build();
            }

            // Ensure the ID matches the existing user
            user.setId(id);
            userService.updateUser(user);

            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    /**
     * Update a user's password.
     *
     * @param id The ID of the user whose password is to be updated.
     * @param passwordRequest The new password wrapped in a PasswordRequest object.
     * @return ResponseEntity indicating the result of the operation.
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<String> updateUserPassword(@PathVariable int id, @RequestBody PasswordRequest passwordRequest) {
        try {
            User user = userService.findById(id);
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

    /**
     * Helper class to handle password updates in the request body.
     */
    public static class PasswordRequest {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    /**
     * Find a user by their username.
     *
     * @param username The username to search for.
     * @return ResponseEntity containing the user or an error message.
     */
    @GetMapping("/username/{username}")
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
}
