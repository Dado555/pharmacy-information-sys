package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.service.AppointmentService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.support.AppointmentToAppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    private final AppointmentToAppointmentDTO toAppointmentDTO;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService, AppointmentToAppointmentDTO toAppointmentDTO) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.toAppointmentDTO = toAppointmentDTO;
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.findAll()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/scheduled", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getScheduledAppointments() {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.findScheduledAppointmentsForPatient(userService.getAuthenticatedUser().getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getAppointmentHistory() {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.findAppointmentHistoryForPatient(userService.getAuthenticatedUser().getId())), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.updateAppointment(appointmentDTO)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/history/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> searchAppointmentHistory(@PathParam("sortBy") String sortBy) {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.getAppointmentHistoryFromAppointmentList(appointmentService.findAllBySearchCriteria(userService.getAuthenticatedUser().getId(), sortBy))), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/scheduled/counseling", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> scheduleAppointmentCounseling(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentService.scheduleAppointmentCounseling(userService.getAuthenticatedUser().getId(), appointmentDTO);
        appointmentService.sendConfirmationMail(appointment.getPatient(), appointment.getId());
        return new ResponseEntity<>(toAppointmentDTO.convert(appointment), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/scheduled/examination", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> scheduleAppointmentExamination(@PathParam("id") Integer id) {
        Appointment appointment = appointmentService.scheduleAppointmentExamination(userService.getAuthenticatedUser().getId(), id);
        appointmentService.sendConfirmationMail(appointment.getPatient(), appointment.getId());
        return new ResponseEntity<>(toAppointmentDTO.convert(appointment), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(value = "/scheduled/counseling/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> cancelAppointmentCounseling(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.cancelAppointmentCounseling(userService.getAuthenticatedUser().getId(), id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(value = "/scheduled/examination/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> cancelAppointmentExamination(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.cancelAppointmentExamination(userService.getAuthenticatedUser().getId(), id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DERMATOLOGIST')")
    @GetMapping(value = "/free/{dermatologistId}/{pharmacyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Appointment>> getFreeAppointments(@PathVariable("dermatologistId") Integer id1, @PathVariable("pharmacyId") Integer id2) {
        return new ResponseEntity<>(appointmentService.getFreeAppointments(id1, id2), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @GetMapping(value = "/done/{medicalWorkerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getDoneAppointments(@PathVariable("medicalWorkerId") Integer id) {
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.getDoneAppointments(id)), HttpStatus.OK);
    }
}
