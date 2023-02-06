package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.Supplier;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    @Query("select supplier from Supplier supplier " +
            "where locate(concat(lower(supplier.firstName), concat(' ', lower(supplier.lastName))), lower(?1)) > 0 " +
            "or locate(lower(?1), concat(lower(supplier.firstName), concat(' ', lower(supplier.lastName)))) > 0")
    List<Supplier> findSuppliersByFirstAndLastName(String firstAndLastName);

    Supplier findSupplierByEmail(String email);
}
