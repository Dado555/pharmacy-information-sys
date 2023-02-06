package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.service.MedicalWorkerService;

@RestController
@CrossOrigin
@RequestMapping("/api/medical-workers")
public class MedicalWorkerController {

    private final MedicalWorkerService medicalWorkerService;

    @Autowired
    public MedicalWorkerController(MedicalWorkerService medicalWorkerService) {
        this.medicalWorkerService = medicalWorkerService;
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PutMapping(value = "/{id}/busy", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> medicalWorkerBusy(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(medicalWorkerService.setBusy(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DERMATOLOGIST', 'PHARMACIST')")
    @PutMapping(value = "/{id}/free", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> medicalWorkerFree(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(medicalWorkerService.setFree(id), HttpStatus.OK);
    }
}
