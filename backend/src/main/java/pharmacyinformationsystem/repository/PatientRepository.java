package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.Patient;


import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("select patient from Patient patient where patient.active = true")
    List<Patient> findAll();

    Patient findByIdAndActiveTrue(Integer id);

    Patient findByEmailAndActiveTrue(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select patient from Patient patient where patient.email = ?1 and patient.active = true")
    Patient findByEmailAndActiveTrueWithLock(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select patient from Patient patient where patient.id = :id and patient.active = true")
    Patient findByIdWithLock(Integer id);

    @Query("select patient from Patient patient " +
            "where locate(concat(lower(patient.firstName), concat(' ', lower(patient.lastName))), lower(?1)) > 0 " +
            "or locate(lower(?1), concat(lower(patient.firstName), concat(' ', lower(patient.lastName)))) > 0")
    List<Patient> findPatientsByFirstAndLastName(String firstAndLastName);
}
