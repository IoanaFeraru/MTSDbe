package org.mastersdbis.mtsd.Entities.User.Provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Provider extends User {

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

    @Size(max = 50)
    @Column(name = "servicedomain", length = 50)
    private ServiceDomain serviceDomain;

    @Size(max = 50)
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$", message = "IBAN-ul trebuie să fie în formatul internațional, începând cu două litere de țară și urmate de cifre.")
    @Column(name = "bankiban", length = 50)
    private String bankIBAN;

    @Enumerated(EnumType.STRING)
    @Column(name = "validationstatus", length = 50)
    private ValidationStatus validationStatus;

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", rating=" + getRating() +
                ", cif='" + cif + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAdress='" + companyAdress + '\'' +
                ", serviceDomain=" + serviceDomain +
                ", bankIBAN='" + bankIBAN + '\'' +
                ", validationStatus=" + validationStatus +
                '}';
    }

    //TODO very very very future Ioana: o lista cu date pe care sa le selecteze furnizorii ca unavailable
}
