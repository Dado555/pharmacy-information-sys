package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.PharmacyAdmin;

import java.util.List;

public interface PharmacyAdminService extends GenericService<PharmacyAdmin> {

    List<PharmacyAdmin> findPharmacyAdminsByFirstAndLastName(String firstAndLastName);

    PharmacyAdmin findPharmacyAdminByEmail(String email);

    PharmacyAdmin createPharmacyAdmin(PharmacyAdmin pharmacyAdmin, Integer pharmacyId);

    Page<PharmacyAdmin> findAll(Integer page);

}
