package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubscriptionDTO {

    private Integer Id;
    private Integer patientId;
    private Integer pharmacyId;
    private String pharmacyName;
}
