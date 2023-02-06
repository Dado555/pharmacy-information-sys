package pharmacyinformationsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.MedicalWorker;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class TimeOffRequest extends BaseEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "start_date", nullable = false)
    private Long startDate;

    @Column(name = "end_date", nullable = false)
    private Long endDate;

    @ManyToOne
    @JoinColumn(name = "medical_worker_id")
    @JsonIgnore
    private MedicalWorker medicalWorker;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;
}
