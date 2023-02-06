package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ComplaintDTO {
    private Integer id;
    private String complaintMessage;
    private String complaintResponse;
    private Integer patientId;
    private String patientEmail;
    private Integer systemAdminId;

    private String entityId;

}
