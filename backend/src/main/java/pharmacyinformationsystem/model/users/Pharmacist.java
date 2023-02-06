package pharmacyinformationsystem.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.Pharmacy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Pharmacist extends MedicalWorker {

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    public Boolean pharmacistDismissal(Integer pharmacyId) {
        if(pharmacy.getId().equals(pharmacyId)) {
            pharmacy = null;
            return true;
        }
        return false;
    }
}
