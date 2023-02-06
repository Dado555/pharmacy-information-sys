package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class PharmacyMedicinePage {
    private List<PharmacyMedicineDTO> medicines;
    private Integer totalPages;
}
