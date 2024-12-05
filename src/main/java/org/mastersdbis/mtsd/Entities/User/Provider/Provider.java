package org.mastersdbis.mtsd.Entities.User.Provider;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Provider")
@Entity
public class Provider extends AbstractEntity {

    @Id
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    @MapsId
    private User user;

    @Size(max = 20)
    @Pattern(regexp = "^RO[0-9]{1,9}[0-9]$", message = "CIF-ul trebuie să fie în formatul RO urmat de 1 până la 9 cifre și o cifră de verificare.")
    @Column(name = "cif", length = 20, unique = true)
    private String cif;

    @Size(max = 100)
    @Column(name = "companyname", length = 100)
    private String companyName;

    @Size(max = 255)
    @Column(name = "companyadress")
    private String companyAdress;

    @NotNull(message = "Service Domain cannot be null")
    @Column(name = "servicedomain")
    private ServiceDomain serviceDomain;

    @Size(max = 50)
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$", message = "IBAN-ul trebuie să fie în formatul internațional, începând cu două litere de țară și urmate de cifre.")
    @Column(name = "bankiban", length = 50)
    private String bankIBAN;

    @Enumerated(EnumType.STRING)
    @Column(name = "validationstatus")
    private ValidationStatus validationStatus;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "approved_by", referencedColumnName = "id", nullable = true)
    private User aprovedBy;

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", user=" + user +
                ", cif='" + cif + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAdress='" + companyAdress + '\'' +
                ", serviceDomain=" + serviceDomain +
                ", bankIBAN='" + bankIBAN + '\'' +
                ", validationStatus=" + validationStatus +
                '}';
    }

    @Override
    public Integer getId() { return id; }
}
    // TODO very very very future Ioana: o lista cu date pe care sa le selecte
