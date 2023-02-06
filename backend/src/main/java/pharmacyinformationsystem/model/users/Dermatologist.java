package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.Dermacy;
import pharmacyinformationsystem.model.WorkSchedule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Dermatologist extends MedicalWorker {

    @OneToMany(mappedBy = "dermatologist", fetch = FetchType.LAZY) //(cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    //@JoinTable(name = "dermacy", joinColumns = @JoinColumn(name = "dermatologist_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "pharmacy_id", referencedColumnName = "id"))
    private List<Dermacy> dermacy;

    public Boolean dermatologistDismissal(Integer pharmacyId) {
        Dermacy delete = null;
        for (Dermacy d: dermacy) {
            if (d.getPharmacy().getId().equals(pharmacyId))
                delete = d;
        }
        return dermacy.remove(delete);
    }
}
