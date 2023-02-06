package pharmacyinformationsystem.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;

@AllArgsConstructor @Getter @Setter
public class UserDTO {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Double discount;
    private AddressDTO address;
    private String role;
    private Boolean active;

    public UserDTO(Integer id, String email, String firstName, String lastName, String phoneNumber, AddressDTO address, String role, Boolean active) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.active = active;
    }
}
