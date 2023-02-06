package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.PharmacyAdmin;

import java.util.List;

@Repository
public interface PharmacyAdminRepository extends JpaRepository<PharmacyAdmin, Integer> {
    @Query("select pharmacyAdmin from PharmacyAdmin pharmacyAdmin " +
            "where locate(concat(lower(pharmacyAdmin.firstName), concat(' ', lower(pharmacyAdmin.lastName))), lower(?1)) > 0 " +
            "or locate(lower(?1), concat(lower(pharmacyAdmin.firstName), concat(' ', lower(pharmacyAdmin.lastName)))) > 0")
    List<PharmacyAdmin> findPharmacyAdminsByFirstAndLastName(String firstAndLastName);

    PharmacyAdmin findPharmacyAdminByEmail(String email);

    @Query("select pharmacyAdmin from PharmacyAdmin pharmacyAdmin " +
            "where pharmacyAdmin.pharmacy.id = ?1")
    PharmacyAdmin findOneByPharmacyId(Integer id);
}
