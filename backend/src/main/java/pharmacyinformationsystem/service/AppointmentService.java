package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import java.util.List;

public interface AppointmentService extends GenericService<Appointment> {

    List<Appointment> findScheduledAppointmentsForPatient(Integer patientId);

    List<Appointment> findAppointmentHistoryForPatient(Integer patientId);

    List<Appointment> findAllBySearchCriteria(Integer patientId, String sortBy);

    List<Appointment> findFreeAppointments(Integer pharmacyId);

    List<Appointment> getAppointmentHistoryFromAppointmentList(List<Appointment> appointmentList);

    Appointment updateAppointment(AppointmentDTO appointmentDTO);

    Appointment scheduleAppointmentCounseling(Integer patientId, AppointmentDTO appointmentDTO);

    Appointment scheduleAppointmentExamination(Integer patientId, Integer appointmentId);

    Appointment cancelAppointmentCounseling(Integer patientId, Integer appointmentId);

    Appointment cancelAppointmentExamination(Integer patientId, Integer appointmentId);

    void sendConfirmationMail(Patient patient, Integer id);

    List<Appointment> getFreeAppointments(Integer dermatologistId, Integer pharmacyId);

    List<Appointment> getDoneAppointments(Integer medicalWorkerId);

    void setPenaltiesForPastAppointments();

    Appointment saveWithLock(Appointment appointment);
}
