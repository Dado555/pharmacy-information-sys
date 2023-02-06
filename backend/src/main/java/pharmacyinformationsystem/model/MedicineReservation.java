package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineReservation extends BaseEntity {

    @Column(name = "date_and_time", nullable = false)
    private Long dateAndTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MedicineReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "medicineReservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineReservationItem> reservationItems = new ArrayList<>();

    public MedicineReservation(Long dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (this.patient != null && !this.patient.getMedicineReservationList().contains(this))
            patient.addMedicineReservation(this);
    }

    public MedicineReservation addReservationItem(MedicineReservationItem item) {
        this.reservationItems.add(item);
        if(!this.equals(item.getMedicineReservation()))
            item.setMedicineReservation(this);
        return this;
    }

}
