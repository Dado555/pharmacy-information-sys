package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter @Setter
public class Subscription extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public Subscription(Patient patient, Pharmacy pharmacy) {
        this.patient = patient;
        this.pharmacy = pharmacy;
    }
}
