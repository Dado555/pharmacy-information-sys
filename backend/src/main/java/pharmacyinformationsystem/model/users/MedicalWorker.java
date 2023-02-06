package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.WorkSchedule;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.ratings.Rateable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor @Getter @Setter
public class MedicalWorker extends User implements Rateable {

    @OneToMany(mappedBy = "medicalWorker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<WorkSchedule> workScheduleList;
    @OneToMany(mappedBy = "medicalWorker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MedicalWorkerRating> ratings;
    @OneToMany(mappedBy = "medicalWorker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointmentList = new ArrayList<>();
    @Column
    private Double rating;
    @Column
    private Boolean hasActiveAppointment;

    public MedicalWorker addAppointment(Appointment appointment) {
        this.appointmentList.add(appointment);
        if (!this.equals(appointment.getMedicalWorker()))
            appointment.setMedicalWorker(this);
        return this;
    }
}
