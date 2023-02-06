package pharmacyinformationsystem.service.base;

import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.service.AppointmentService;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AppointmentServiceBase implements AppointmentService  {

    @Override
    public List<Appointment> getAppointmentHistoryFromAppointmentList(List<Appointment> appointmentList) {
        return appointmentList.stream()
                              .filter(a -> a.getEndDateAndTime() <= System.currentTimeMillis()).collect(Collectors.toList());
    }
}
