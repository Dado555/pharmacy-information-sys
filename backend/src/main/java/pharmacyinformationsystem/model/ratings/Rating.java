package pharmacyinformationsystem.model.ratings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;

@Entity
@Table(name="ratings")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public abstract class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "date_time", nullable = false)
    private Long dateTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Rating(Integer value, Patient patient, Long datetime) {
        this.value = value;
        this.patient = patient;
        this.dateTime = datetime;
    }
}