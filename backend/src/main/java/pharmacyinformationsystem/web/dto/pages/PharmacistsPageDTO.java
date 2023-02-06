package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.PharmacistDTO;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class PharmacistsPageDTO {
    List<PharmacistDTO> pharmacists;
    private Integer totalPages;
}
