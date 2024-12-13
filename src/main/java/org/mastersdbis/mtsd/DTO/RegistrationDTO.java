package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    @NotNull(message = "Username is required.")
    @Size(max = 50, message = "Username cannot exceed 50 characters.")
    private String username;

    @NotNull(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 100, message = "Email cannot exceed 100 characters.")
    private String email;

    @NotNull(message = "Phone number is required.")
    @Pattern(regexp = "^\\+40 [0-9]{9}$", message = "Phone number must be in the format +40 xxxxxxxxx.")
    @Size(max = 15, message = "Phone number cannot exceed 15 characters.") // Aligns with entity constraint
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;

    @NotNull(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
}
