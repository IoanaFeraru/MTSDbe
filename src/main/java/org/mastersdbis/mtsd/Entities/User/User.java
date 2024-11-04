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
@Table(name = "Users")
public abstract class User extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 100)
    @NotNull
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Size(max = 100)
    @NotNull
    @Email
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 12)
    @NotNull
    @Pattern(regexp = "^\\+40 [0-9]{3} [0-9]{3} [0-9]{3}$", message = "Numărul de telefon trebuie să fie în formatul +40 xxx xxx xxx.")
    @Column(name = "phonenumber", nullable = false, length = 12)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Positive(message = "Rating-ul trebuie să fie un număr pozitiv.")
    @Max(value = 5, message = "Rating-ul trebuie să fie între 0 și 5.")
    @Column(name = "rating")
    private Double rating;

    @Size(max = 255)
    @Column(name = "notificationpreferences")
    private NotificationPreference notificationPreferences;

    @Override
    public Integer getId() { return id; }
}