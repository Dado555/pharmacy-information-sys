package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentReportDTO;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/appointment-reports")
public class AppointmentReportController {

    private final AppointmentReportService appointmentReportService;

    @Autowired
    public AppointmentReportController(AppointmentReportService appointmentReportService) {
        this.appointmentReportService = appointmentReportService;
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentReport> createAppointmentReport(@RequestBody AppointmentReportDTO appointmentReportDTO) {
        return new ResponseEntity<>(appointmentReportService.createAppointmentReport(appointmentReportDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PostMapping(value = "/new-appointment", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> createNewAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return new ResponseEntity<>(appointmentReportService.createNewAppointment(appointmentDTO), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PutMapping(value = "/appointments/{id}/reserve", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Appointment> reserveAppointment(@RequestBody Map<String, Integer> patientIdMap, @PathVariable("id") Integer id) {
        return new ResponseEntity<>(appointmentReportService.reserveAppointment(id, patientIdMap.get("patientId")), HttpStatus.OK);
    }
}
