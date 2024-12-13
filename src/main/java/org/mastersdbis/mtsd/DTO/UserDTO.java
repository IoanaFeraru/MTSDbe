package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotNull(message = "Username-ul este obligatoriu")
    private String username;

    @NotNull(message = "Parola este obligatorie")
    private String password;

    @NotNull(message = "Email-ul este obligatoriu")
    @Email(message = "Email-ul nu este valid")
    private String email;

    @NotNull(message = "Numărul de telefon este obligatoriu")
    private String phoneNumber;
}
