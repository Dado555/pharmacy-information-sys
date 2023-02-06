package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.model.Complaint;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity @Getter @Setter
public class SystemAdmin extends User {

    @OneToMany(mappedBy = "systemAdmin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Complaint> complaints;

    public SystemAdmin() {
        super();
    }

    public SystemAdmin addComplaint(Complaint complaint) {
        this.complaints.add(complaint);
        if (!this.equals(complaint.getSystemAdmin()))
            complaint.setSystemAdmin(this);
        return this;
    }
}
