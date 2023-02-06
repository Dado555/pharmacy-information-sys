package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Appointment;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    @Query("select app from Appointment app where app.patient.id = ?1 and app.appointmentStatus = 'RESERVED'")
    List<Appointment> findScheduledAppointmentsForPatient(Integer patientId);

    @Query("select app from Appointment app where app.patient.id = ?1 and (app.appointmentStatus = 'CANCELED' or app.appointmentStatus = 'DONE')")
    List<Appointment> findAppointmentHistoryForPatient(Integer patientId);

    @Query("select app from Appointment app where app.patient.id = ?1 order by app.startDateAndTime asc")
    List<Appointment> findAllByPatientAndOrderByStartDateAndTimeAsc(Integer id);
    @Query("select app from Appointment app where app.patient.id = ?1 order by app.startDateAndTime desc")
    List<Appointment> findAllByPatientAndOrderByStartDateAndTimeDesc(Integer id);
    @Query("select app from Appointment app where app.patient.id = ?1 order by app.price asc")
    List<Appointment> findAllByPatientAndOrderByPriceAsc(Integer id);
    @Query("select app from Appointment app where app.patient.id = ?1 order by app.price desc")
    List<Appointment> findAllByPatientAndOrderByPriceDesc(Integer id);
    @Query("select app from Appointment app where app.pharmacy.id = ?1 and app.appointmentStatus = 'FREE'")
    List<Appointment> findFreeAppointments(Integer pharmacyId);

    @Query("select app from Appointment app where app.medicalWorker.id = ?1 and app.pharmacy.id = ?2 and app.appointmentStatus = 'FREE'" +
            "and app.startDateAndTime > ?3")
    List<Appointment> findFreeAppointments(Integer dermatologistId, Integer pharmacyId, Long currentTime);

    @Query("select app from Appointment app where app.medicalWorker.id = ?1 and app.appointmentStatus = 'DONE'")
    List<Appointment> findDoneAppointments(Integer medicalWorkerId);

    @Query("select app from Appointment app where app.patient.id = ?3 and ((app.startDateAndTime <= ?1 and app.endDateAndTime >= ?1) or " +
            "(app.startDateAndTime <= ?2 and app.endDateAndTime >= ?2) or (app.startDateAndTime >= ?1 and app.endDateAndTime <= ?2))")
    List<Appointment> findOverlappingForPatient(Long startDate, Long endDate, Integer patientId);
    @Query("select app from Appointment app where app.medicalWorker.id = ?3 and ((app.startDateAndTime <= ?1 and app.endDateAndTime >= ?1) or " +
            "(app.startDateAndTime <= ?2 and app.endDateAndTime >= ?2) or (app.startDateAndTime >= ?1 and app.endDateAndTime <= ?2))")
    List<Appointment> findOverlappingForMedicalWorker(Long startDate, Long endDate, Integer medicalWorkerId);

    Appointment findAppointmentById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Appointment> findByMedicalWorkerId(Integer medicalWorkerId);
}
