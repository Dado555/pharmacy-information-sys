package pharmacyinformationsystem.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.PatientCategory;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class PatientDTO {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private AddressDTO address;
    private String role;
    private List<PenaltyDTO> penalties;
    private Boolean active;
    private AppointmentDTO appointment;
    private Double points;
    private PatientCategory category;

    public PatientDTO(Integer id, String email, String firstName, String lastName, String phoneNumber, AddressDTO addressDTO, List<PenaltyDTO> penalties, String role, Boolean active, Double points, PatientCategory category) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = addressDTO;
        this.penalties = penalties;
        this.role = role;
        this.active = active;
        this.points = points;
        this.category = category;
    }
}
