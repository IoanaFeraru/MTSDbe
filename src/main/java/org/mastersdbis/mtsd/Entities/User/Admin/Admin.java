package org.mastersdbis.mtsd.Entities.User.Admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.User.User;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin extends AbstractEntity {

    @Id
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    @MapsId
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<Permission> permissions;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getId() +
                ", permissions=" + permissions +
                '}';
    }
}
