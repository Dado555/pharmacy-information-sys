package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pharmacyinformationsystem.model.ratings.PharmacyRating;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Pharmacy extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(nullable = false)
    private Long startWorkTime;

    @Column(nullable = false)
    private Long endWorkTime;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "counseling_price", nullable = false)
    private Double counselingPrice;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PharmacyMedicine> pharmacyMedicineList;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pharmacist> pharmacists;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PharmacyAdmin> pharmacyAdmins;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY) //, cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Dermacy> dermacy;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PharmacyRating> ratings;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id")
    private Point point;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TimeOffRequest> timeOffRequests;

    @OneToMany(mappedBy = "pharmacy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineInquiry> medicineInquiries;

    @Version
    @ColumnDefault("0")
    private Short version;

    public Pharmacy addPharmacyMedicine(PharmacyMedicine pharmacyMedicine) {
        this.pharmacyMedicineList.add(pharmacyMedicine);
        if (this.equals(pharmacyMedicine.getPharmacy()))
            pharmacyMedicine.setPharmacy(this);
        return this;
    }

    public Boolean pharmacistDismissal(Integer id) {
        Pharmacist delete = null;
        for (Pharmacist p: pharmacists) {
            if (p.getId().equals(id)) {
                delete = p;
                delete.pharmacistDismissal(super.getId());
            }
        }
        return pharmacists.remove(delete);
    }

    public Boolean dermatologistDismissal(Integer id) {
        Dermacy delete = null;
        for (Dermacy d: dermacy){
            if (d.getDermatologist().getId().equals(id)) {
                delete = d;
                delete.getDermatologist().dermatologistDismissal(super.getId());
            }
        }
        return dermacy.remove(delete);
    }

    public List<Dermatologist> getDermatologists() {
        List<Dermatologist> dermatologists = new ArrayList<>();
        for(Dermacy d: dermacy) {
            dermatologists.add(d.getDermatologist());
        }
        return dermatologists;
    }
}
