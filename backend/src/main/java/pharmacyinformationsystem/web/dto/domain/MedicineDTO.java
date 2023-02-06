package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.MedicineForm;
import pharmacyinformationsystem.model.enums.MedicineIssuingType;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineDTO {

    private Integer id;
    private String name;
    private String type;
    private MedicineForm medicineForm;
    private String structure;
    private String manufacture;
    private MedicineIssuingType medicineIssuingType;
    private double dailyIntake;
    private String contraindications;
    private Double rating;
    private Boolean rateable;
    private Integer patientRating;

}
