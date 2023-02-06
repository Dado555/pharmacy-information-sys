package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppointmentReportDTO {
    String appointmentInfo;
    Integer patientId;
    Integer appointmentId;
    List<PrescriptionMedicineDTO> medicines;
}
