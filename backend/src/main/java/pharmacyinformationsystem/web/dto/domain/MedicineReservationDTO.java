package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineReservationDTO {

    private Integer id;
    private List<MedicineReservationItemDTO> medicineReservationItemDTOList;
    private Long dateAndTime;
    private Double price;
    private MedicineReservationStatus status;

}
