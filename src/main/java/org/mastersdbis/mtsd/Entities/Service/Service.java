package org.mastersdbis.mtsd.Entities.Service;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.mastersdbis.mtsd.Entities.AbstractEntity;
import org.mastersdbis.mtsd.Entities.User.Provider.Provider;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "service")
public class Service extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_id_gen")
    @SequenceGenerator(name = "service_id_gen", sequenceName = "service_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull(message = "Provider cannot be null.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false, referencedColumnName = "id")
    private Provider provider;

    @NotNull(message = "Name cannot be null.")
    @Size(max = 100, message = "Name must not exceed 100 characters.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = Integer.MAX_VALUE, message = "Description must not exceed maximum length.")
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull(message = "Domain cannot be null.")
    @Enumerated(EnumType.STRING)
    @Size(max = 100, message = "Domain must not exceed 100 characters.")
    @Column(name = "domain", length = 100)
    private ServiceDomain domain;

    @Enumerated(EnumType.STRING)
    @Size(max = 100, message = "Subdomain must not exceed 100 characters.")
    @Column(name = "subdomain", length = 100)
    private ServiceSubdomain subdomain;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @NotNull(message = "Price cannot be null.")
    @Column(name = "price")
    private Double price;

    @NotNull(message = "Region cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "region", length = 50)
    private Region region;

    @Size(max = Integer.MAX_VALUE, message = "Materials must not exceed maximum length.")
    @Column(name = "materials", length = Integer.MAX_VALUE)
    private String materials;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    //ToDO: Far future daca ma plictisesc: report

    @Override
    public Integer getId() { return id; }

    //Un string care are o lista de materiale separate prin virgule;
    public List<String> getMaterialsList() {
        if (materials != null && !materials.isEmpty()) {
            return Arrays.asList(materials.split(",\\s*"));
        }
        return List.of();
    }

    public void setMaterialsList(List<String> materialsList) {
        this.materials = String.join(", ", materialsList);
    }
}
