package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.EPrescriptionMedicine;

@Repository
public interface EPrescriptionMedicineRepository extends JpaRepository<EPrescriptionMedicine, Integer> {
}
