package pharmacyinformationsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import pharmacyinformationsystem.service.AppointmentService;

@Controller
public class SchedulerController {

    private final AppointmentService appointmentService;

    @Autowired
    public SchedulerController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void setPenaltiesForPastAppointments() {
        appointmentService.setPenaltiesForPastAppointments();
    }
}
