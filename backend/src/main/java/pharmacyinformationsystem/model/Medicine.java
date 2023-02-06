package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pharmacyinformationsystem.model.enums.MedicineForm;
import pharmacyinformationsystem.model.enums.MedicineIssuingType;
import pharmacyinformationsystem.model.ratings.MedicineRating;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Medicine extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "medicine_form", nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicineForm medicineForm;

    @Column(name = "structure", nullable = false)
    private String structure;

    @Column(name = "manufacture", nullable = false)
    private String manufacture;

    @Column(name = "medicine_issuing_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MedicineIssuingType medicineIssuingType;

    @Column(name = "daily_intake", nullable = false)
    private Double dailyIntake;

    @Column(name = "contraindications", nullable = false)
    private String contraindications;

    @Column(name = "points", nullable = false)
    private Double points;

    @Column(name = "rating")
    private Double rating;

    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineRating> ratings;

    @OneToMany(mappedBy = "medicine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineInquiry> medicineInquiries;

    @OneToMany
    private List<Medicine> replacementMedicines;

    @ManyToMany(mappedBy = "allergicMedicines")
    private List<Patient> patients;

    @Version
    @ColumnDefault("0")
    private Short version;

    public void update(Medicine medicine) {
        this.name = medicine.getName();
        this.type = medicine.getType();
        this.medicineForm = medicine.getMedicineForm();
        this.structure = medicine.getStructure();
        this.manufacture = medicine.getManufacture();
        this.medicineIssuingType = medicine.getMedicineIssuingType();
        this.dailyIntake = medicine.getDailyIntake();
        this.contraindications = medicine.getContraindications();
    }
}
