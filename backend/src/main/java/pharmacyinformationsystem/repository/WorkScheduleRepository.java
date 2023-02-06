package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.WorkSchedule;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {

    @Query("select ws from WorkSchedule ws where ws.pharmacy.id = ?1 and ws.medicalWorker.id = ?2")
    WorkSchedule findByPharmacyIdAndMedicalWorkerId(Integer pharmacyId, Integer MedicalWorkerId);
}
