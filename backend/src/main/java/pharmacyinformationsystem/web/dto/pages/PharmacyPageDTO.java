package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.domain.PharmacyDTO;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class PharmacyPageDTO {

    private List<PharmacyDTO> pharmacies;
    private Integer totalPages;

}
