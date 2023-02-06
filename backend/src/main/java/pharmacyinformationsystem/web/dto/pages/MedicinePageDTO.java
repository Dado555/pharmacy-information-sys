package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.domain.MedicineDTO;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class MedicinePageDTO {

    List<MedicineDTO> medicines;
    private Integer totalPages;
}
