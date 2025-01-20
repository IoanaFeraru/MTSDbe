package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;
import org.mastersdbis.mtsd.Entities.User.Provider.ValidationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDTO {
    private Integer id;

    @NotNull(message = "User ID cannot be null.")
    private int userId;

    @Size(max = 20, message = "CIF cannot exceed 20 characters.")
    @Pattern(regexp = "^RO[0-9]{1,9}[0-9]$", message = "CIF-ul trebuie să fie în formatul RO urmat de 1 până la 9 cifre și o cifra de verificare.")
    private String cif;

    @Size(max = 100, message = "Company name cannot exceed 100 characters.")
    private String companyName;

    @Size(max = 255, message = "Company address cannot exceed 255 characters.")
    private String companyAdress;

    @NotNull(message = "Service Domain cannot be null.")
    private ServiceDomain serviceDomain;

    @Size(max = 50, message = "IBAN cannot exceed 50 characters.")
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$", message = "IBAN-ul trebuie să fie în formatul internațional, începând cu două litere de țară și urmate de cifre.")
    private String bankIBAN;

    private ValidationStatus validationStatus;

    private Integer approvedByUserId;

    public ProviderDTO(Provider provider) {
        this.id = provider.getId();
        this.userId = provider.getUser().getId();
        this.cif = provider.getCif();
        this.companyName = provider.getCompanyName();
        this.companyAdress = provider.getCompanyAdress();
        this.serviceDomain = provider.getServiceDomain();
        this.bankIBAN = provider.getBankIBAN();
        this.validationStatus = provider.getValidationStatus();
        this.approvedByUserId = provider.getAprovedBy().getId();
    }
}