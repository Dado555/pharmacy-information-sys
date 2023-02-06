package pharmacyinformationsystem.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pharmacyinformationsystem.model.EPrescription;
import pharmacyinformationsystem.model.enums.EPrescriptionStatus;

import java.util.List;


public interface EPrescriptionRepository extends JpaRepository<EPrescription, Integer> {

    @Query("select p from EPrescription p where p.patient.id = ?1")
    List<EPrescription> filterByPatient(Integer patientId, Pageable pageable);

    @Query("select p from EPrescription p where p.patient.id = ?1 and p.status = ?2")
    List<EPrescription> filterByPatientAndStatus(Integer patientId, EPrescriptionStatus status, Pageable pageable);
}
