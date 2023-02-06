package pharmacyinformationsystem.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.SystemAdmin;

import java.util.List;

public interface ComplaintService extends GenericService<Complaint> {

    List<Complaint> getComplaintsForSystemAdmin(Integer id);
    List<Complaint> getComplaintsWithoutSystemAdmin();
    Complaint updateComplaint(Complaint complaint, Integer id);
    Boolean sendConfirmationMail(Complaint complaint);
}
