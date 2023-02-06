package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.Pharmacy;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class PharmacyAdmin extends User{

    @OneToMany(mappedBy = "pharmacyAdmin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name="pharmacy_id")
    private Pharmacy pharmacy;
}
