package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.PatientCategory;


public interface PatientCategoryRepository extends JpaRepository<PatientCategory, Integer> {

}
