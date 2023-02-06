package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.Dermacy;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.service.DermacyService;
import pharmacyinformationsystem.service.DermatologistService;
import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.service.WorkScheduleService;
import pharmacyinformationsystem.support.UserToUserDTO;
import pharmacyinformationsystem.web.dto.users.UserDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/dermacy")
public class DermacyController {

    private final UserToUserDTO userToUserDTO;
    private final DermacyService dermacyService;
    private final DermatologistService dermatologistService;
    private final PharmacyService pharmacyService;
    private final WorkScheduleService workScheduleService;

    @Autowired
    public DermacyController(UserToUserDTO userToUserDTO, DermacyService dermacyService, DermatologistService dermatologistService, PharmacyService pharmacyService, WorkScheduleService workScheduleService) {
        this.userToUserDTO = userToUserDTO;
        this.dermacyService = dermacyService;
        this.dermatologistService = dermatologistService;
        this.pharmacyService = pharmacyService;
        this.workScheduleService = workScheduleService;
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @DeleteMapping(value = "/{dermatologistId}/{pharmacyId}")
    public ResponseEntity<Boolean> dermatologistDismissal(@PathVariable("dermatologistId") Integer dermatologistId, @PathVariable("pharmacyId") Integer pharmacyId) {
        Dermacy p = dermacyService.findOneByDermatologistIdAndPharmacyId(dermatologistId, pharmacyId);
        if(p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        dermacyService.dermatologistDismissal(dermatologistId, pharmacyId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/hire-dermatologist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> hireDermatologist(@RequestBody WorkScheduleDTO workSchedule) {
        Dermatologist dermatologist = dermatologistService.findOne(workSchedule.getDermatologistId());
        Pharmacy pharmacy = pharmacyService.findOne(workSchedule.getPharmacyId());
        if(dermatologist == null || pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        dermacyService.createNew(workSchedule.getPharmacyId(), workSchedule.getDermatologistId());
        workScheduleService.createNew(workSchedule.getPharmacyId(), workSchedule.getDermatologistId(),
                                      workSchedule.getStart(), workSchedule.getEnd());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/dermatologist={id}-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<List<Long>>> getDermatologistSchedule(@PathVariable("id") Integer dermatologistId) {
        Dermatologist dermatologist = dermatologistService.findOne(dermatologistId);
        if(dermatologist == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(dermatologistService.getTakenAppointments(dermatologistId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/can-employ-pharmacy={id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUnemployedDermatologists(@PathVariable("id") Integer pharmacyId) {
        List<User> dermatologists = new ArrayList<>(dermatologistService.findEmployable(pharmacyId));
        return new ResponseEntity<>(userToUserDTO.convert(dermatologists), HttpStatus.OK);
    }
}
