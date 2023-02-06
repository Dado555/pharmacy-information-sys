package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.AppointmentToAppointmentDTO;
import pharmacyinformationsystem.support.DermatologistToDermatologistDTO;
import pharmacyinformationsystem.support.UserToUserDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.pages.DermatologistsPageDTO;
import pharmacyinformationsystem.web.dto.users.DermatologistDTO;
import pharmacyinformationsystem.web.dto.users.UserDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/dermatologists")
public class DermatologistController {

    private final DermatologistService dermatologistService;
    private final UserToUserDTO userToUserDTO;
    private final AppointmentToAppointmentDTO appointmentToAppointmentDTO;
    private final AddressService addressService;
    private final DermatologistToDermatologistDTO dermatologistToDermatologistDTO;
    private final RoleService roleService;
    private final DermacyService dermacyService;

    @Autowired
    public DermatologistController(DermatologistService dermatologistService, UserToUserDTO userToUserDTO,
                                   AppointmentToAppointmentDTO appointmentToAppointmentDTO, AddressService addressService,
                                   DermatologistToDermatologistDTO dermatologistToDermatologistDTO, RoleService roleService, DermacyService dermacyService) {
        this.dermatologistService = dermatologistService;
        this.userToUserDTO = userToUserDTO;
        this.appointmentToAppointmentDTO = appointmentToAppointmentDTO;
        this.addressService = addressService;
        this.dermatologistToDermatologistDTO = dermatologistToDermatologistDTO;
        this.roleService = roleService;
        this.dermacyService = dermacyService;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getDermatologists() {
        List<User> dermatologists = new ArrayList<User>(dermatologistService.findAll());
        return new ResponseEntity<>(userToUserDTO.convert(dermatologists), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'SYSTEM_ADMIN')")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistsPageDTO> getDermatologistsPage(@RequestParam Optional<Integer> page) {
        Page<Dermatologist> dermatologists = dermatologistService.findAll(page.orElse(0));
        List<Dermatologist> dermatologistList = dermatologists.toList();
        for(Dermatologist d: dermatologistList) {
            d.setDermacy(dermacyService.dermacies(d.getId()));
        }
        return new ResponseEntity<>(new DermatologistsPageDTO(
                dermatologistToDermatologistDTO.convert(dermatologistList),
                dermatologists.getTotalPages()) , HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACIST', 'DERMATOLOGIST')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getOneDermatologist(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userToUserDTO.convert(dermatologistService.findOne(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getDermatologistRatingsMonth(@PathVariable("id") Integer id) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if(dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dermatologistService.getRatingsMonthly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getDermatologistRatingsYear(@PathVariable("id") Integer id) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if(dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dermatologistService.getRatingsYearly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACIST', 'DERMATOLOGIST', 'PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(appointmentToAppointmentDTO.convert(dermatologistService.getAppointments(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/work-schedule/{pharmacyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkScheduleDTO> getSchedule(@PathVariable("id") Integer id, @PathVariable("pharmacyId") Integer pharmacyId) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if (dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        WorkScheduleDTO workScheduleDTO = dermatologistService.getWorkSchedule(pharmacyId, id);
        if(workScheduleDTO == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(workScheduleDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PostMapping(value = "/new-appointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createAppointment(@RequestBody AppointmentDTO appointment) {
        Boolean ret = dermatologistService.createAppointment(appointment);
        if(!ret)
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/appointments-reserved-future/worker={id}/pharmacy={pharId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getFutureReservedAppointments(@PathVariable("id") Integer id, @PathVariable("pharId") Integer pharmacyId) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if (dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(appointmentToAppointmentDTO.convert(dermatologistService.getFutureAppointments(pharmacyId, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistDTO> createDermatologist(@RequestBody Dermatologist dermatologist) {
        return new ResponseEntity<>(dermatologistToDermatologistDTO.convert(dermatologistService.createDermatologist(dermatologist)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('DERMATOLOGIST')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateDermatologist(@RequestBody Dermatologist dermatologist, @PathVariable Integer id) {
        return new ResponseEntity<>(userToUserDTO.convert(dermatologistService.update(dermatologist, id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deleteDermatologist(@PathVariable("id") Integer id) {
        User u = dermatologistService.findOne(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        u.setActive(false);
        return new ResponseEntity<>(userToUserDTO.convert(u), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/{pharmacyId}")
    public ResponseEntity<Boolean> dermatologistDismissal(@PathVariable("id") Integer id, @PathVariable("pharmacyId") Integer pharmacyID) {
        Dermatologist d = dermatologistService.findOne(id);
        if(d == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(d.dermatologistDismissal(pharmacyID), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DermatologistsPageDTO> searchDermatologists(@RequestParam Optional<Integer> page, @RequestBody MedicalWorkerSearchParameters searchParameters) {
        Page<Dermatologist> dermatologists = dermatologistService.searchByCriteria(searchParameters, Optional.of(page.orElse(0)));
        List<Dermatologist> dermatologistList = dermatologists.toList();
        for(Dermatologist d: dermatologistList) {
            d.setDermacy(dermacyService.dermacies(d.getId()));
        }
        return new ResponseEntity<>(new DermatologistsPageDTO(
                dermatologistToDermatologistDTO.convert(dermatologistList),
                dermatologists.getTotalPages()) , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/appointments-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getAppointmentsMonthly(@PathVariable("id") Integer id) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if(dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> appointments = appointmentToAppointmentDTO.doneAppointmentsMonthly(dermatologist.getAppointmentList());
        return new ResponseEntity<>(dermatologistService.getAppointmentsMonthly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/appointments-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getAppointmentsYearly(@PathVariable("id") Integer id) {
        Dermatologist dermatologist = dermatologistService.findOne(id);
        if(dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> appointments = appointmentToAppointmentDTO.doneAppointmentsYearly(dermatologist.getAppointmentList());
        return new ResponseEntity<>(dermatologistService.getAppointmentsYearly(id), HttpStatus.OK);
    }
}
