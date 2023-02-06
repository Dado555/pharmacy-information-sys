package pharmacyinformationsystem.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;

import java.util.List;

@AllArgsConstructor @Getter @Setter
public class DermatologistDTO {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private AddressDTO address;
    private String role;
    private Boolean active;
    private Double rating;
    private List<Integer> pharmacyIds;
    private Boolean rateable;
    private Integer patientRating;
}
