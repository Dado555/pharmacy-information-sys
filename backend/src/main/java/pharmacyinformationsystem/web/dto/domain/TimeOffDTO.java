package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class TimeOffDTO {
    private Integer id;
    private String content;
    private Boolean approved;
    private Long startDate;
    private Long endDate;
    private Integer medicalWorkerId;
}
