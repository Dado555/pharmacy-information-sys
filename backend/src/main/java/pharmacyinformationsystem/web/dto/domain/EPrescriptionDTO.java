package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.EPrescriptionStatus;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class EPrescriptionDTO {

    private Integer id;
    private Long dateOfIssue;
    private EPrescriptionStatus status;
    private List<EPrescriptionMedicineDTO> ePrescriptionMedicineDTOList;
}
