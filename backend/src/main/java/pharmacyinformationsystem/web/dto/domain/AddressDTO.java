package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class AddressDTO {

    private Integer id;
    private String name;
    private String city;
    private String number;
    private String postalCode;
    private Double longitude;
    private Double latitude;
}
