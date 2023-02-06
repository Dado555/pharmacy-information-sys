package pharmacyinformationsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.SystemAdmin;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Complaint extends BaseEntity {

    @Column(name = "complaint_message")
    private String complaintMessage;

    @Column(name = "complaint_response")
    private String complaintResponse;

    @Column(name = "entity", nullable = false)
    private String entity;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "system_admin_id")
    @JsonIgnore
    private SystemAdmin systemAdmin;

    public Complaint(String complaintMessage, String entity) {
        this.complaintMessage = complaintMessage;
        this.entity = entity;
    }

    public Complaint(Patient patient, String complaintMessage, String entity) {
        this.patient = patient;
        this.complaintMessage = complaintMessage;
        this.entity = entity;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (this.patient != null && !this.patient.getComplaintList().contains(this))
            patient.addComplaint(this);
    }

    public void setSystemAdmin(SystemAdmin systemAdmin) {
        this.systemAdmin = systemAdmin;
        if (this.systemAdmin != null && !this.systemAdmin.getComplaints().contains(this))
            systemAdmin.addComplaint(this);
    }

    public void update(String complaintResponse) {
        this.complaintResponse = complaintResponse;
    }
}
