package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class EPrescriptionMedicineDTO {

    private Integer id;
    private String name;
    private Integer amount;
    private Integer therapy;
}
