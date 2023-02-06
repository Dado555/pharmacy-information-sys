package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.TimeOffRequest;

import java.util.List;

@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, Integer> {

    @Query("select tr from TimeOffRequest tr where tr.approved = true and tr.medicalWorker.id = ?3 and ((tr.startDate <= ?1 and tr.endDate >= ?1) or " +
            "(tr.startDate <= ?2 and tr.endDate >= ?2) or (tr.startDate >= ?1 and tr.endDate <= ?2))")
    List<TimeOffRequest> findOverlappingForMedicalWorker(Long startDate, Long endDate, Integer medicalWorkerId);
}

