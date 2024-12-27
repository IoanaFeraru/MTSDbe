package org.mastersdbis.mtsd.Controllers;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.mastersdbis.mtsd.DTO.ProviderDTO;
import org.mastersdbis.mtsd.DTO.ProviderUpdateDTO;
import org.mastersdbis.mtsd.DTO.UserDTO;
import org.mastersdbis.mtsd.DTO.UserUpdateDTO;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Role;
import org.mastersdbis.mtsd.Entities.User.User;
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

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        if (userUpdateDTO.getUsername() != null) {
            user.setUsername(userUpdateDTO.getUsername());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        }
        if (userUpdateDTO.getAddress() != null) {
            user.setAddress(userUpdateDTO.getAddress());
        }

        userService.updateUser(user);
        return ResponseEntity.ok("User updated successfully.");
    }

    @PatchMapping("/{username}/password")
    public ResponseEntity<?> updatePassword(@PathVariable String username, @RequestBody PasswordRequest passwordRequest) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        userService.updateUserPassword(user, passwordRequest.getPassword());
        return ResponseEntity.ok("Password updated successfully.");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    //http://localhost:8080/users?usernamePattern=exemplu
    @GetMapping
    public ResponseEntity<?> searchUsers(@RequestParam String usernamePattern) {
        if (usernamePattern.length() < 3) {
            return ResponseEntity.badRequest().body("The search pattern must contain at least 3 characters.");
        }

        List<User> users = userService.searchByUsernamePattern(usernamePattern);
        if (users.isEmpty()) {
            return ResponseEntity.ok("No users found matching the pattern.");
        }
        return ResponseEntity.ok(users);
    }

    //de testat
    @PostMapping("/providers")
    public ResponseEntity<?> addProvider(@RequestBody @Valid ProviderDTO providerDTO) {
        Provider provider = new Provider();
        provider.setUser(userService.findById(providerDTO.getUserId()));
        provider.setCif(providerDTO.getCif());
        provider.setCompanyName(providerDTO.getCompanyName());
        provider.setCompanyAdress(providerDTO.getCompanyAdress());
        provider.setServiceDomain(providerDTO.getServiceDomain());
        provider.setBankIBAN(providerDTO.getBankIBAN());

        userService.addProvider(provider);
        return ResponseEntity.status(HttpStatus.CREATED).body("Provider added successfully.");
    }

    @PutMapping("/providers/{username}")
    public ResponseEntity<?> updateProvider(@PathVariable String username, @RequestBody ProviderUpdateDTO providerUpdateDTO) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Provider provider = userService.findProviderByUser(user);
        if (provider == null) {
            return ResponseEntity.badRequest().body("The specified user is not a provider.");
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
    }

    //De retestat
    @PutMapping("/providers/{providerId}/validate")
    public ResponseEntity<?> validateProvider(@PathVariable Integer providerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        User adminUser = userService.findByUsername(authentication.getName());
        if (adminUser == null || adminUser.getRoles().stream().noneMatch(role -> role.equals(Role.ADMIN))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have ADMIN role.");
        }

        Provider provider = userService.findById(providerId);
        if (provider == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Provider not found.");
        }

        adminService.validateProvider(provider, adminUser);
        return ResponseEntity.ok("Provider validated successfully.");
    }

    @GetMapping("/providers/{username}")
    public ResponseEntity<?> findProvider(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            Provider provider = userService.findProviderByUser(user);
            if (provider == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No provider associated with this user.");
            }

            ProviderDTO providerDTO = new ProviderDTO(provider);
            return ResponseEntity.ok(providerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving provider: " + e.getMessage());
        }
    }

    @GetMapping("/id")
    public ResponseEntity<?> getCurrentUserId() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No authenticated user found.");
            }
            return ResponseEntity.ok(user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving user ID: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllUsers() {
        try {
            List<User> users = userService.findAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.ok("No users found.");
            }

            List<UserDTO> userDTOs = users.stream()
                    .map(UserDTO::new)
                    .toList();

            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users: " + e.getMessage());
        }
    }

    @GetMapping("/providers/all")
    public ResponseEntity<?> findAllProviders() {
        try {
            List<Provider> providers = userService.findAllProviders();
            if (providers.isEmpty()) {
                return ResponseEntity.ok("No providers found.");
            }

            List<ProviderDTO> providerDTOs = providers.stream()
                    .filter(provider -> provider.getAprovedBy() != null)
                    .map(ProviderDTO::new)
                    .toList();

            if (providerDTOs.isEmpty()) {
                return ResponseEntity.ok("No approved providers found.");
            }

            return ResponseEntity.ok(providerDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving providers: " + e.getMessage());
        }
    }

    //De retestat
    @PutMapping("/providers/{providerId}/deny")
    public ResponseEntity<String> denyProvider(@PathVariable("providerId") Integer providerId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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

    //Is the auth user a provider? Get method

    @Getter
    @Setter
    public static class PasswordRequest {
        private String password;
    }
}
