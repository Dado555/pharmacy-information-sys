package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.users.SupplierDTO;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SupplierPageDTO {
    List<SupplierDTO> suppliers;
    private Integer totalPages;
}
