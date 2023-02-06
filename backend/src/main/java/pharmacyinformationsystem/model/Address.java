package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.User;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Address extends BaseEntity {

    @Column(name = "street_name", nullable = false)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pharmacy> pharmacies;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;

    public Address(String name, String city, String number, String postalCode, Double longitude, Double latitude) {
        this.name = name;
        this.city = city;
        this.number = number;
        this.postalCode = postalCode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void update(String city, String name, String number, String postalCode) {
        this.city = city;
        this.name = name;
        this.number = number;
        this.postalCode = postalCode;
    }
}
