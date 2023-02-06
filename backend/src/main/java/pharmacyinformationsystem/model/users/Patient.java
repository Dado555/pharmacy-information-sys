package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class Patient extends User {

    @ManyToMany
    @JoinTable(name = "patient_allergic_medicine", joinColumns = @JoinColumn(name = "patient_id"), inverseJoinColumns = @JoinColumn(name = "medicine_id"))
    private List<Medicine> allergicMedicines = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicineReservation> medicineReservationList = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EPrescription> ePrescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Complaint> complaintList = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Penalty> penalties = new ArrayList<>();

    @Column
    private Double points;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_category_id")
    private PatientCategory patientCategory;

    public Patient addComplaint(Complaint complaint) {
        this.complaintList.add(complaint);
        if (!this.equals(complaint.getPatient()))
            complaint.setPatient(this);
        return this;
    }

    public Patient addMedicineReservation(MedicineReservation reservation) {
        this.medicineReservationList.add(reservation);
        if (!this.equals(reservation.getPatient()))
            reservation.setPatient(this);
        return this;
    }

    public Patient addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
        if (!this.equals(appointment.getPatient()))
            appointment.setPatient(this);
        return this;
    }

    public Patient addAllergicMedicine(Medicine medicine) {
        this.allergicMedicines.add(medicine);
        return this;
    }

    public Patient addEPrescription(EPrescription ePrescription) {
        this.ePrescriptions.add(ePrescription);
        if (!this.equals(ePrescription.getPatient()))
            ePrescription.setPatient(this);
        return this;
    }

    public Boolean addSubscription(Subscription subscription) {
        for (Subscription sub : this.subscriptions) {
            if (sub.getPatient() == subscription.getPatient() && sub.getPharmacy() == subscription.getPharmacy()) {
                return false;
            }
        }
        this.subscriptions.add(subscription);
        if (!this.equals(subscription.getPatient()))
            subscription.setPatient(this);
        return true;
    }

    public Patient addPenalty(Penalty penalty) {
        this.penalties.add(penalty);
        if (!this.equals(penalty.getPatient()))
            penalty.setPatient(this);
        return this;
    }

    public Boolean deleteSubscription(Subscription subscription) {
        if (!this.subscriptions.contains(subscription)) {
            return false;
        }
        this.subscriptions.remove(subscription);
        return true;
    }
}

