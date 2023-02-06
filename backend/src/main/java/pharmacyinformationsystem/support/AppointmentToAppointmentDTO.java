package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AppointmentToAppointmentDTO implements Converter<Appointment, AppointmentDTO> {
    @Override
    public AppointmentDTO convert(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), appointment.getAppointmentType(),
                appointment.getAppointmentStatus(), appointment.getStartDateAndTime(), appointment.getEndDateAndTime(),
                appointment.getPrice(), "", appointment.getPharmacy().getName(), appointment.getMedicalWorker().getFirstName() + " " + appointment.getMedicalWorker().getLastName(), 0,
                appointment.getMedicalWorker().getId(), appointment.getPharmacy().getId());
        Patient patient = appointment.getPatient();
        if (patient != null) {
            appointmentDTO.setPatient(patient.getFirstName() + " " + patient.getLastName());
            appointmentDTO.setPatientId(patient.getId());
        }
        return appointmentDTO;
    }

    public List<AppointmentDTO> convert(List<Appointment> appointmentList) {
        return appointmentList.stream().map(this::convert).collect(Collectors.toList());
    }
}
