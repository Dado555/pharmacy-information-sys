package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.PrescriptionMedicine;

@Repository
public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, Integer> {
}
