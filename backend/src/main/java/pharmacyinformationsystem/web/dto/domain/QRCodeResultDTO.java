package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QRCodeResultDTO {
    private Integer id;
    private EPrescriptionDTO ePrescriptionDTO;
    private PharmacyDTO pharmacyDTO;
    private Double totalPrice;
}
