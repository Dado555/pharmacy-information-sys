package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.PharmacyMedicine;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.service.PharmacyMedicineService;
import pharmacyinformationsystem.support.PharmacyMedicineToPharmacyMedicineDTO;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/pharmacy-medicines")
public class PharmacyMedicineController {

    private final PharmacyMedicineService pharmacyMedicineService;
    private final PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO;

    @Autowired
    public PharmacyMedicineController(PharmacyMedicineService pharmacyMedicineService, PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO) {
        this.pharmacyMedicineService = pharmacyMedicineService;
        this.pharmacyMedicineToPharmacyMedicineDTO = pharmacyMedicineToPharmacyMedicineDTO;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyMedicineDTO>> getPharmacyMedicines(@RequestParam Optional<Integer> page) {
        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(pharmacyMedicineService.findAll()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyMedicineDTO> getOnePharmacyMedicine(@PathVariable("id") Integer id) {
        PharmacyMedicine pharmacyMedicine = pharmacyMedicineService.findOne(id);
        if (pharmacyMedicine == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(pharmacyMedicine), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(value = "/pharmacies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyMedicineDTO>> getPharmaciesForMedicine(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(pharmacyMedicineService.findPharmaciesForMedicine(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @GetMapping(value = "/{id}/replacement-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyMedicineDTO>> getReplacementMedicines(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(pharmacyMedicineService.findReplacementMedicines(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> prescribe(@RequestBody Map<String, Integer> patientAmount, @PathVariable Integer id) {
        return new ResponseEntity<>(pharmacyMedicineService.prescribeMedicine(patientAmount, id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PutMapping(value = "/{id}/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> removeMedicine(@RequestBody Map<String, Integer> patientAmount, @PathVariable Integer id) {
        return new ResponseEntity<>(pharmacyMedicineService.removeMedicine(patientAmount, id), HttpStatus.OK);
    }
}
