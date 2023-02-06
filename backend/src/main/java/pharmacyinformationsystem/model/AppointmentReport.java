package pharmacyinformationsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class AppointmentReport extends BaseEntity {

    @Column(name = "appointment_info", nullable = false)
    private String appointmentInfo;

    @OneToOne
    @JsonIgnore
    private Prescription prescription;
}
