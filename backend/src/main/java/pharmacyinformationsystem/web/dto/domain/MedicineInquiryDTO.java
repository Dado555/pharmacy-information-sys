package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineInquiryDTO {
    private Integer id;
    private Long dateCreated;
    private Boolean resolved;
    private Integer medicineId;
    private Integer pharmacyId;
}
