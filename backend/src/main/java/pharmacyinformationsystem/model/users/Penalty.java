package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor @Getter @Setter
public class Penalty extends BaseEntity {

    @Column(name = "date", nullable = false)
    private Long date;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Penalty(Long date, String description, Patient patient) {
        this.date = date;
        this.description = description;
        this.patient = patient;
    }
}
