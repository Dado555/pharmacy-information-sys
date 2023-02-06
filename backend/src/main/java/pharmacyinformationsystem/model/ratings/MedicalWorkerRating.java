package pharmacyinformationsystem.model.ratings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor @Getter @Setter
public class MedicalWorkerRating extends Rating {

    @ManyToOne
    @JoinColumn(name = "medical_worker_id")
    private MedicalWorker medicalWorker;

    public MedicalWorkerRating(Integer value, Patient patient, Long dateTime, MedicalWorker medicalWorker) {
        super(value, patient, dateTime);
        this.medicalWorker = medicalWorker;
    }
}
