package pharmacyinformationsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.model.users.Patient;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Appointment extends BaseEntity {

    @Column(name = "start_date_and_time", nullable = false)
    private Long startDateAndTime;

    @Column(name = "end_date_and_time", nullable = false)
    private Long endDateAndTime;

    @Column(name = "price", nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false)
    private AppointmentStatus appointmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private AppointmentType appointmentType;

    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    @JsonIgnore
    private Pharmacy pharmacy;

    @ManyToOne
    @JoinColumn(name = "medical_worker_id")
    @JsonIgnore
    private MedicalWorker medicalWorker;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @OneToOne
    private AppointmentReport appointmentReport;

    @Version
    @ColumnDefault("0")
    private Short version;

    public Appointment(Long startDateAndTime, Long endDateAndTime, Double price, AppointmentStatus appointmentStatus, AppointmentType appointmentType, Patient patient, MedicalWorker medicalWorker, Pharmacy pharmacy) {
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.price = price;
        this.appointmentStatus = appointmentStatus;
        this.appointmentType = appointmentType;
        this.setPatient(patient);
        this.setMedicalWorker(medicalWorker);
        this.pharmacy = pharmacy;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (this.patient != null && !this.patient.getAppointments().contains(this))
            patient.addAppointment(this);
    }

    public void setMedicalWorker(MedicalWorker medicalWorker) {
        this.medicalWorker = medicalWorker;
        if (this.medicalWorker != null && !this.medicalWorker.getAppointmentList().contains(this))
            medicalWorker.addAppointment(this);
    }
}
