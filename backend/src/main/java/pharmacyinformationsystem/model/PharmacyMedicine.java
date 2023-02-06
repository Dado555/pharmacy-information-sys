package pharmacyinformationsystem.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PharmacyMedicine extends BaseEntity {

    @Column(name = "price")
    private Double price;

    @Column(name = "available_amount")
    private Integer availableAmount;

    @Column(name = "action_price")
    private Double actionPrice;

    @Column(name = "until_date_time")
    private Long untilDateTime;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @OneToMany(mappedBy = "pharmacyMedicine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineReservationItem> reservationItems = new ArrayList<>();

    @OneToMany(mappedBy = "pharmacyMedicine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PharmacyMedicinePrice> medicinePrices;

    @Version
    @ColumnDefault("0")
    private Short version;
}
