package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PharmacyMedicinePrice extends BaseEntity {

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date")
    private Long endDate;

    @ManyToOne
    @JoinColumn(name = "pharmacy_medicine_id")
    private PharmacyMedicine pharmacyMedicine;

    @OneToMany(mappedBy = "pharmacyMedicinePrice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<PharmedActionPrice> pharmedActionPrices;

    public PharmacyMedicinePrice(Double price, Long startDate, PharmacyMedicine pharmacyMedicine) {
        this.price = price;
        this.startDate = startDate;
        this.pharmacyMedicine = pharmacyMedicine;
    }

    public void addActionPrice(PharmedActionPrice actionPrice) {
        this.pharmedActionPrices.add(actionPrice);
    }
}
