package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class EPrescriptionMedicine extends BaseEntity {

    @Column(name = "therapy_duration", nullable = false)
    private Integer therapyDuration;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "eprescription_id")
    private EPrescription ePrescription;
}
