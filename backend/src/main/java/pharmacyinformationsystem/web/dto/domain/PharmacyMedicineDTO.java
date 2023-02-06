package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PharmacyMedicineDTO {

    private Integer id;
    private PharmacyDTO pharmacyDTO;
    private MedicineDTO medicineDTO;
    private Double price;
    private Integer availableAmount;
    private Double actionPrice;
    private Long untilDateTime;
    private Double totalPriceAfterQR;

    public PharmacyMedicineDTO(Integer id, PharmacyDTO pharmacyDTO, MedicineDTO medicineDTO, Double price, Integer availableAmount, Double actionPrice, Long untilDateTime) {
        this.id = id;
        this.pharmacyDTO = pharmacyDTO;
        this.medicineDTO = medicineDTO;
        this.price = price;
        this.availableAmount = availableAmount;
        this.actionPrice = actionPrice;
        this.untilDateTime = untilDateTime;
    }

}
