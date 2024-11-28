package org.mastersdbis.mtsd.Entities.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Users")
public class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 100)
    @NotNull
    @Email
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Size(max = 15)
    @NotNull
    @Pattern(regexp = "^\\+40 [0-9]{9}$", message = "Numărul de telefon trebuie să fie în formatul +40 xxxxxxxxx.")
    @Column(name = "phonenumber", nullable = false, length = 12)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Max(value = 5, message = "Rating-ul trebuie să fie maxim 5.")
    @Min(value = 0, message = "Rating-ul trebuie sa fie minim 0")
    @Column(name = "rating")
    private Double rating;

    @Override
    public Integer getId() { return id; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                '}';
    }
}