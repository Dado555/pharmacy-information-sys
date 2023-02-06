package pharmacyinformationsystem.web.dto.pages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.users.SystemAdminDTO;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class SystemAdminPageDTO {
    List<SystemAdminDTO> systemAdmins;
    private Integer totalPages;
}
