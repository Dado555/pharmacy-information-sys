package pharmacyinformationsystem.model.ratings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.Pharmacy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor @Getter @Setter
public class PharmacyRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public PharmacyRating(Integer value, Patient patient, Long dateTime, Pharmacy pharmacy) {
        super(value, patient, dateTime);
        this.pharmacy = pharmacy;
    }
}