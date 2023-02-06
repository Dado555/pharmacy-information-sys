package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineReservationItem extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "issued", nullable = false)
    private Boolean issued = false;

    @Column(name = "discount")
    private Double discount = 0D;

    @ManyToOne
    @JoinColumn(name = "medicine_reservation_id")
    private MedicineReservation medicineReservation;

    @ManyToOne
    @JoinColumn(name = "pharmacy_medicine_id")
    private PharmacyMedicine pharmacyMedicine;

    @Version
    @ColumnDefault("0")
    private Short version;

    public MedicineReservationItem(PharmacyMedicine pharmacyMedicine, Integer amount) {
        this.pharmacyMedicine = pharmacyMedicine;
        this.amount = amount;
    }

    public void setMedicineReservation(MedicineReservation reservation) {
        this.medicineReservation = reservation;
        if (this.medicineReservation != null && !this.medicineReservation.getReservationItems().contains(this))
            reservation.addReservationItem(this);
    }
}
