package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.MedicalWorker;

@Repository
public interface MedicalWorkerRepository extends JpaRepository<MedicalWorker, Integer> {
}
