package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.SystemAdmin;

import java.util.List;

public interface SystemAdminService extends GenericService<SystemAdmin> {
    List<SystemAdmin> findSystemAdminsByFirstAndLastName(String firstAndLastName);

    SystemAdmin findSystemAdminByEmail(String email);

    SystemAdmin createSystemAdmin(SystemAdmin systemAdmin);

    Page<SystemAdmin> findAll(Integer page);

    List<Complaint> systemAdminComplaints(Integer id);
}
