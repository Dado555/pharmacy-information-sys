package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.PharmacyMedicinePrice;

public interface PharmedPriceRepository extends JpaRepository<PharmacyMedicinePrice, Integer> {
}
