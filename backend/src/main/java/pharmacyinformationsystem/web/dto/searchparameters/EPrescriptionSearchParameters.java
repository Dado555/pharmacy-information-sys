package pharmacyinformationsystem.web.dto.searchparameters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class EPrescriptionSearchParameters {

    private String sortBy;
    private String orderBy;
    private String filterBy;
}
