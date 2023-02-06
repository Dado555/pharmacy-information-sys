package pharmacyinformationsystem.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;
import pharmacyinformationsystem.web.dto.domain.ComplaintDTO;

@AllArgsConstructor @Getter @Setter
public class SystemAdminDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private AddressDTO address;
    private String role;
    private Boolean active;
    private ComplaintDTO complaintDTO;

    public SystemAdminDTO(Integer id, String email, String firstName, String lastName, String phoneNumber, AddressDTO address, String role, Boolean active) {
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
