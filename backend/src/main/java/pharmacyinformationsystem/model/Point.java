package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Point extends BaseEntity {

    @Column(name = "pharmacist_counseling")
    private Double pharmacistCounseling;

    @Column(name = "dermatologist_examination")
    private Double dermatologistExamination;

    @OneToMany(mappedBy = "point", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pharmacy> pharmacies;
}
