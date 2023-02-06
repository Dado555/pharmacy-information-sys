package pharmacyinformationsystem.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class PharmacyMedicineEdit {
    Integer id;
    String name;
    String type;
    Double price;
    Integer available;
}
