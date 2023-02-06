package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.users.PharmacyAdminDTO;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PharmacyAdminPageDTO {
    List<PharmacyAdminDTO> pharmacyAdmins;
    private Integer totalPages;
}
