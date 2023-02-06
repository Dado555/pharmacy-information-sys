package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;

import java.util.List;

public interface PharmacistService extends GenericService<Pharmacist>, AdvancedSearchService<Pharmacist, MedicalWorkerSearchParameters> {
    Page<Pharmacist> findAll(Integer page);

    void pharmacistDismissal(Integer id);

    List<Pharmacist> findAllByPharmacyIsNull();

    Pharmacist findOneWithLock(Integer id);

    void hirePharmacist(Integer id, Integer pharmacyId);

    User update(Pharmacist pharmacist, Integer id);

    List<Appointment> getAppointments(Integer id);

    List<Double> getRatingsMonthly(Integer id);

    List<Double> getRatingsYearly(Integer id);

    List<Integer> getAppointmentsMonthly(Integer id);

    List<Integer> getAppointmentsYearly(Integer id);

    WorkScheduleDTO getWorkSchedule(Integer pharmacyId, Integer pharmacistId);

    Boolean createAppointment(AppointmentDTO appointmentDTO);

    List<Appointment> getFutureAppointments(Integer pharmacyId, Integer pharmacistId);

    Pharmacist deleteById(Integer id);

    Pharmacist findPharmacistByEmail(String email);
}
