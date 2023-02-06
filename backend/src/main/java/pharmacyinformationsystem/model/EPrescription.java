package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.EPrescriptionStatus;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "e_prescription")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class EPrescription extends BaseEntity {

    @Column(name = "date_of_issue", nullable = false)
    private Long dateOfIssue;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EPrescriptionStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "ePrescription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EPrescriptionMedicine> ePrescriptionMedicineList;

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (this.patient != null && !this.patient.getEPrescriptions().contains(this))
            patient.addEPrescription(this);
    }

}
