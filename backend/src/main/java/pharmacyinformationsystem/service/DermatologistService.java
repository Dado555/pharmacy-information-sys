package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;

import java.util.List;

public interface DermatologistService extends GenericService<Dermatologist>, AdvancedSearchService<Dermatologist, MedicalWorkerSearchParameters>{
    Page<Dermatologist> findAll(Integer page);

    List<Dermatologist> findEmployable(Integer pharmacyId);

    Dermatologist findDermatologistByEmail(String email);

    User update(Dermatologist dermatologist, Integer id);

    List<Appointment> getAppointments(Integer id);

    List<List<Long>> getTakenAppointments(Integer derId);

    List<Double> getRatingsMonthly(Integer id);

    List<Double> getRatingsYearly(Integer id);

    List<Integer> getAppointmentsMonthly(Integer id);

    List<Integer> getAppointmentsYearly(Integer id);

    WorkScheduleDTO getWorkSchedule(Integer pharmacyId, Integer dermatologistId);

    Boolean createAppointment(AppointmentDTO appointmentDTO);

    List<Appointment> getFutureAppointments(Integer pharmacyId, Integer dermatologistId);

    Dermatologist deleteById(Integer id);

    Dermatologist createDermatologist(Dermatologist dermatologist);
}
