package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.service.AppointmentService;
import pharmacyinformationsystem.service.PharmacistService;
import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.service.WorkScheduleService;
import pharmacyinformationsystem.support.AppointmentToAppointmentDTO;
import pharmacyinformationsystem.support.PharmacistToPharmacistDTO;
import pharmacyinformationsystem.support.UserToUserDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;
import pharmacyinformationsystem.web.dto.pages.PharmacistsPageDTO;
import pharmacyinformationsystem.web.dto.users.UserDTO;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@RestController
@CrossOrigin
@RequestMapping("/api/pharmacists")
public class PharmacistController {

    private final PharmacistService pharmacistService;
    private final UserToUserDTO userToUserDTO;
    private final AppointmentToAppointmentDTO appointmentToAppointmentDTO;
    private final PharmacyService pharmacyService;
    private final PharmacistToPharmacistDTO pharmacistToPharmacistDTO;
    private final WorkScheduleService workScheduleService;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService, UserToUserDTO userToUserDTO,
                                AppointmentToAppointmentDTO appointmentToAppointmentDTO, PharmacyService pharmacyService,
                                PharmacistToPharmacistDTO pharmacistToPharmacistDTO, WorkScheduleService workScheduleService){
        this.pharmacistService = pharmacistService;
        this.userToUserDTO = userToUserDTO;
        this.appointmentToAppointmentDTO = appointmentToAppointmentDTO;
        this.pharmacyService = pharmacyService;
        this.pharmacistToPharmacistDTO = pharmacistToPharmacistDTO;
        this.workScheduleService = workScheduleService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getPharmacists() {
        List<User> pharmacists = new ArrayList<User>(pharmacistService.findAll());
        return new ResponseEntity<>(userToUserDTO.convert(pharmacists), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacistsPageDTO> getPharmacistsPage(@RequestParam Optional<Integer> page) {
        Page<Pharmacist> pharmacists = pharmacistService.findAll(page.orElse(0));
        return new ResponseEntity<>(new PharmacistsPageDTO(
                pharmacistToPharmacistDTO.convert(pharmacists.toList()),
                pharmacists.getTotalPages()) , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/unemployed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnemployedPharmacists() {
        List<User> pharmacists = new ArrayList<User>(pharmacistService.findAllByPharmacyIsNull());
        return new ResponseEntity<>(userToUserDTO.convert(pharmacists), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getOnePharmacist(@PathVariable("id") Integer id) {
        User user = pharmacistService.findOne(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userToUserDTO.convert(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getPharmacistRatingsMonth(@PathVariable("id") Integer id) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if(pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacistService.getRatingsMonthly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getPharmacistRatingsYear(@PathVariable("id") Integer id) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if(pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacistService.getRatingsYearly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACIST', 'SYSTEM_ADMIN', 'PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(appointmentToAppointmentDTO.convert(pharmacistService.getAppointments(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/work-schedule/{pharmacyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkScheduleDTO> getSchedule(@PathVariable("id") Integer id, @PathVariable("pharmacyId") Integer pharmacyId) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if (pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        WorkScheduleDTO workScheduleDTO = pharmacistService.getWorkSchedule(pharmacyId, id);
        if(workScheduleDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(workScheduleDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{id}/pharmacy={pharmacyId}/start={startTime}/end={endTime}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> hirePharmacist(@PathVariable("id") Integer id, @PathVariable("pharmacyId") Integer pharmacyId,
                              @PathVariable("startTime") Long startTime, @PathVariable("endTime") Long endTime) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        Pharmacy pharmacy = pharmacyService.findOne(pharmacyId);
        if(pharmacist == null || pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        pharmacistService.hirePharmacist(id, pharmacyId);
        WorkSchedule ws = new WorkSchedule(startTime, endTime, pharmacy, pharmacist);
        workScheduleService.save(ws);
        pharmacist.setPharmacy(pharmacyService.findOne(pharmacyId));

        return new ResponseEntity<>(userToUserDTO.convert(pharmacist), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/appointments-reserved-future/worker={id}/pharmacy={pharId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getFutureReservedAppointments(@PathVariable("id") Integer id, @PathVariable("pharId") Integer pharmacyId) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if (pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(appointmentToAppointmentDTO.convert(pharmacistService.getFutureAppointments(pharmacyId, id)), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createPharmacist(@RequestBody Pharmacist pharmacist) {
        User savedUser = pharmacistService.save(pharmacist);
        if (savedUser == null)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(userToUserDTO.convert(savedUser), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PostMapping(value = "/new-appointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createAppointment(@RequestBody AppointmentDTO appointment) {
        Boolean ret = pharmacistService.createAppointment(appointment);
        if(!ret)
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePharmacist(@RequestBody Pharmacist pharmacist, @PathVariable Integer id) {
        return new ResponseEntity<>(userToUserDTO.convert(pharmacistService.update(pharmacist, id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deletePharmacist(@PathVariable("id") Integer id) {
        User u = pharmacistService.findOne(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        u.setActive(false);
        return new ResponseEntity<>(userToUserDTO.convert(u), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @DeleteMapping(value = "/{id}/{pharmacyId}")
    public ResponseEntity<Boolean> dermatologistDismissal(@PathVariable("id") Integer id, @PathVariable("pharmacyId") Integer pharmacyID) {
        Pharmacist p = pharmacistService.findOne(id);
        if(p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        pharmacistService.pharmacistDismissal(id);
        return new ResponseEntity<>(p.pharmacistDismissal(pharmacyID), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacistsPageDTO> searchPharmacists(@RequestParam Optional<Integer> page, @RequestBody MedicalWorkerSearchParameters searchParameters) {
        Page<Pharmacist> pharmacists = pharmacistService.searchByCriteria(searchParameters, Optional.of(page.orElse(0)));
        return new ResponseEntity<>(new PharmacistsPageDTO(
                pharmacistToPharmacistDTO.convert(pharmacists.toList()),
                pharmacists.getTotalPages()) , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/appointments-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getAppointmentsMonthly(@PathVariable("id") Integer id) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if(pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> appointments = appointmentToAppointmentDTO.doneAppointmentsMonthly(pharmacist.getAppointmentList());
        return new ResponseEntity<>(pharmacistService.getAppointmentsMonthly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/appointments-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getAppointmentsYearly(@PathVariable("id") Integer id) {
        Pharmacist pharmacist = pharmacistService.findOne(id);
        if(pharmacist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> appointments = appointmentToAppointmentDTO.doneAppointmentsYearly(pharmacist.getAppointmentList());
        return new ResponseEntity<>(pharmacistService.getAppointmentsYearly(id), HttpStatus.OK);
    }
}
