package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateDTO {
    @Size(max = 50)
    private String username;

    @Size(max = 100)
    @Email
    private String email;

    @Size(max = 15)
    @Pattern(regexp = "^\\+40 [0-9]{9}$", message = "Numărul de telefon trebuie să fie în formatul +40 xxxxxxxxx.")
    private String phoneNumber;

    @Size(max = 255)
    private String address;
}
