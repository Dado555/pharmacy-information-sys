package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.AppointmentReport;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentReportDTO;

public interface AppointmentReportService extends GenericService<AppointmentReport>{
    AppointmentReport createAppointmentReport(AppointmentReportDTO appointmentReportDTO);

    Appointment createNewAppointment(AppointmentDTO appointmentDTO);

    Appointment reserveAppointment(Integer id, Integer patientId);
}
