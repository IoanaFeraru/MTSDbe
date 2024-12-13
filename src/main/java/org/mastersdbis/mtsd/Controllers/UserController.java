package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.mastersdbis.mtsd.DTO.ProviderDTO;
import org.mastersdbis.mtsd.DTO.ProviderUpdateDTO;
import org.mastersdbis.mtsd.DTO.UserUpdateDTO;
import org.mastersdbis.mtsd.Entities.User.Role;
import org.mastersdbis.mtsd.Entities.User.User;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Services.AdminService;
import org.mastersdbis.mtsd.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public UserController(UserService userService, AdminService adminService) {
            this.userService = userService;
            this.adminService = adminService;
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

    @PutMapping("/providers/update/{username}")
    public ResponseEntity<String> updateProviderByUsername(@PathVariable String username, @RequestBody ProviderUpdateDTO providerUpdateDTO) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }
            Provider provider = userService.findProviderByUser(user);
            if (provider == null) {
                return ResponseEntity.status(400).body("The specified user is not a provider.");
            }

            if (providerUpdateDTO.getCompanyAdress() != null) {
                provider.setCompanyAdress(providerUpdateDTO.getCompanyAdress());
            }
            if (providerUpdateDTO.getServiceDomain() != null) {
                provider.setServiceDomain(providerUpdateDTO.getServiceDomain());
            }
            if (providerUpdateDTO.getBankIBAN() != null) {
                provider.setBankIBAN(providerUpdateDTO.getBankIBAN());
            }
            userService.addProvider(provider);
            return ResponseEntity.ok("Provider updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating provider: " + e.getMessage());
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

    @GetMapping("/id")
    public ResponseEntity<?> getCurrentUserId() {
        try {
            User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user == null) {
                return ResponseEntity.status(404).body("No authenticated user found.");
            }
            return ResponseEntity.ok(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving user ID: " + e.getMessage());
        }
    }

    @GetMapping("/search/all")
    public ResponseEntity<?> findAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.ok("No users found.");
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving users: " + e.getMessage());
        }
    }

    @PutMapping("/validateProvider/{providerId}")
    public ResponseEntity<String> validateProvider(
            @PathVariable("providerId") Integer providerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authenticated user roles: " + authentication.getAuthorities());

            if (!authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            String username = authentication.getName();
            User adminUser = userService.findByUsername(username);
            if (adminUser == null || adminUser.getRoles().stream().noneMatch(role -> role.equals(Role.ADMIN))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have ADMIN role.");
            }

            Provider provider = userService.findById(providerId);
            if (provider == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found.");
            }

            adminService.validateProvider(provider, adminUser);

            return ResponseEntity.ok("Provider validated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/search/providers/all")
    public ResponseEntity<?> findAllProviders() {
        try {
            List<Provider> providers = userService.findAllProviders();
            if (providers.isEmpty()) {
                return ResponseEntity.ok("No providers found.");
            }
            return ResponseEntity.ok(providers);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving providers: " + e.getMessage());
        }
    }

    @GetMapping("/provider/id")
    public ResponseEntity<?> getCurrentProviderId() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(404).body("No authenticated user found.");
            }
            Provider provider = userService.findProviderByUser(user);
            if (provider == null) {
                return ResponseEntity.status(404).body("No provider found for the authenticated user.");
            }
            return ResponseEntity.ok(provider.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error retrieving provider ID: " + e.getMessage());
        }
    }

    @PutMapping("/denyProvider/{providerId}")
    public ResponseEntity<String> denyProvider(
            @PathVariable("providerId") Integer providerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authenticated user roles: " + authentication.getAuthorities());

            if (!authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
            }

            String username = authentication.getName();
            User adminUser = userService.findByUsername(username);

            if (adminUser == null || adminUser.getRoles().stream().noneMatch(role -> role.equals(Role.ADMIN))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have ADMIN role.");
            }

            Provider provider = userService.findById(providerId);
            if (provider == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found.");
            }

            adminService.denyProvider(provider, adminUser);

            return ResponseEntity.ok("Provider denied successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Deny error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
