package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;

@Entity
@Table(name="qrCodeResults")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class QRCodeResult extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="pharmacy_id")
    private Pharmacy pharmacy;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "eprescription_id")
    private EPrescription ePrescription;
}
