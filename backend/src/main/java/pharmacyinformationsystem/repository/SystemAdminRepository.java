package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.SystemAdmin;

import java.util.List;

@Repository
public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Integer> {
    @Query("select systemAdmin from SystemAdmin systemAdmin " +
            "where locate(concat(lower(systemAdmin.firstName), concat(' ', lower(systemAdmin.lastName))), lower(?1)) > 0 " +
            "or locate(lower(?1), concat(lower(systemAdmin.firstName), concat(' ', lower(systemAdmin.lastName)))) > 0")
    List<SystemAdmin> findSystemAdminsByFirstAndLastName(String firstAndLastName);

    SystemAdmin findSystemAdminByEmail(String email);
}
