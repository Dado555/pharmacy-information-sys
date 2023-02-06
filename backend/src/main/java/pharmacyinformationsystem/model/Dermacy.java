package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Dermatologist;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter @Setter
public class Dermacy extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "dermatologist_id")
    private Dermatologist dermatologist;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public Dermacy(Dermatologist dermatologist1, Pharmacy pharmacy1) {
        this.dermatologist = dermatologist1;
        this.pharmacy = pharmacy1;
    }
}
