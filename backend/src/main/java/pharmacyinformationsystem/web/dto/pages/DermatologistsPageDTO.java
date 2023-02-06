package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.users.DermatologistDTO;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class DermatologistsPageDTO {
    List<DermatologistDTO> dermatologists;
    private Integer totalPages;
}
