package pharmacyinformationsystem.model.ratings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor @Getter @Setter
public class MedicineRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    public MedicineRating(Integer value, Patient patient, Long dateTime, Medicine medicine) {
        super(value, patient, dateTime);
        this.medicine = medicine;
    }
}
