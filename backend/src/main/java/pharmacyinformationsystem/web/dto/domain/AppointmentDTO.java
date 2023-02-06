package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class AppointmentDTO {

    private Integer id;
    private AppointmentType title;
    private AppointmentStatus appointmentStatus;
    private Long start;
    private Long end;
    private Double price;
    private String patient;
    private String pharmacy;
    private String medicalWorker;
    private Integer patientId;
    private Integer workerId;
    private Integer pharmacyId;
}
