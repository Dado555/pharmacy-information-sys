package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter @Setter
public class PharmedActionPrice extends BaseEntity {

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date", nullable = false)
    private Long endDate;

    @ManyToOne
    @JoinColumn(name = "pharmacy_medicine_price_id")
    private PharmacyMedicinePrice pharmacyMedicinePrice;

    public PharmedActionPrice(Double price, Long startDate, Long endDate, PharmacyMedicinePrice pharmacyMedicinePrice) {
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pharmacyMedicinePrice = pharmacyMedicinePrice;
    }
}
