package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.MedicineInquiry;

@Repository
public interface MedicineInquiryRepository extends JpaRepository<MedicineInquiry, Integer> {
}
