package pharmacyinformationsystem.web.dto.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineReservationItemDTO {

    private Integer id;
    private PharmacyMedicineDTO pharmacyMedicineDTO;
    private Integer amount;
    private Integer reservationId;
    private Double price;
    private Long dateAndTime;
    private String patientName;
    private Double discount;
}
