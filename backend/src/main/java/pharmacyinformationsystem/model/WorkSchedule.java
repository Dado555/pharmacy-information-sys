package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.WorkShift;
import pharmacyinformationsystem.model.users.MedicalWorker;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class WorkSchedule extends BaseEntity {

    @Column(name = "start_time", nullable = false)
    private Long startTime;     // 8:30h = 8*60+30

    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @Column(name = "work_shift")
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;
    @ManyToOne
    @JoinColumn(name = "medical_worker_id")
    private MedicalWorker medicalWorker;

    public WorkSchedule(Long startTime, Long endTime, Pharmacy pharmacy, MedicalWorker medicalWorker) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pharmacy = pharmacy;
        this.medicalWorker = medicalWorker;
    }

}
