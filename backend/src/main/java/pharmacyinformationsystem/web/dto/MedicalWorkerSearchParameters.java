package pharmacyinformationsystem.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class MedicalWorkerSearchParameters {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String orderBy;
    private String sortBy;
    private List<Integer> pharmacyIds;
    private Integer ratingFrom;
    private Integer ratingTo;
}
