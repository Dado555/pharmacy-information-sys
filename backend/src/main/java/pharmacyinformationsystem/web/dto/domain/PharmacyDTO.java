package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PharmacyDTO {

    private Integer id;
    private String name;
    private AddressDTO address;
    private Double rating;
    private Long startWorkTime;
    private Long endWorkTime;
    private PointDTO point;
    private Double counselingPrice;
    private Boolean rateable;
    private Integer patientRating;

    public PharmacyDTO(Integer id, String name, AddressDTO address, Double rating, Long startWorkTime, Long endWorkTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.startWorkTime = startWorkTime;
        this.endWorkTime = endWorkTime;
    }

}
