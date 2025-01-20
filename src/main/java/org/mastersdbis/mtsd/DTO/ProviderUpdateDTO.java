package org.mastersdbis.mtsd.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mastersdbis.mtsd.Entities.Service.ServiceDomain;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderUpdateDTO {

    @Size(max = 50)
    private String username;

    @Size(max = 255, message = "Company address cannot exceed 255 characters.")
    private String companyAdress;

    @Size(max = 50, message = "IBAN cannot exceed 50 characters.")
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$", message = "IBAN-ul trebuie să fie în formatul internațional, începând cu două litere de țară și urmat de cifre.")
    private String bankIBAN;

    private ServiceDomain serviceDomain;

    @Size(max = 100, message = "Company name cannot exceed 100 characters.")
    private String companyName;

    @Size(max = 20, message = "CIF cannot exceed 20 characters.")
    private String cif;
}
