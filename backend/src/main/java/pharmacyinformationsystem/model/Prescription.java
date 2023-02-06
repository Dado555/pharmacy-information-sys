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
@Table(name = "prescription")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prescription extends BaseEntity {

    @Column(name = "date_of_issue", nullable = false)
    private Long dateOfIssue;

    @OneToMany(mappedBy = "prescription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PrescriptionMedicine> prescriptionMedicineList;

}