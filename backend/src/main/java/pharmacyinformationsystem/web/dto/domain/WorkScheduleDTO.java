package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class WorkScheduleDTO {

    Integer id;
    Integer dermatologistId;
    Integer pharmacyId;
    Long start;
    Long end;
}
