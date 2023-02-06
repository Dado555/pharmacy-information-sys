package pharmacyinformationsystem.web.dto.searchparameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class MedicineSearchParameters {

    private String name;
    private String type;
    private Integer ratingFrom;
    private Integer ratingTo;
    private String sortBy;
    private String orderBy;
    private String filterBy;
    private Integer pharmacyId;

}
