package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.service.EPrescriptionService;
import pharmacyinformationsystem.support.EPrescriptionMedToEPrescriptionMedDTO;
import pharmacyinformationsystem.support.EPrescriptionToEPrescriptionDTO;
import pharmacyinformationsystem.web.dto.domain.EPrescriptionDTO;
import pharmacyinformationsystem.web.dto.domain.EPrescriptionMedicineDTO;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/e-prescriptions")
public class EPrescriptionController {

    private final EPrescriptionService ePrescriptionService;

    private final EPrescriptionToEPrescriptionDTO ePrescriptionToEPrescriptionDTO;
    private final EPrescriptionMedToEPrescriptionMedDTO ePrescriptionMedToEPrescriptionMedDTO;

    @Autowired
    public EPrescriptionController(EPrescriptionService ePrescriptionService, EPrescriptionToEPrescriptionDTO ePrescriptionToEPrescriptionDTO, EPrescriptionMedToEPrescriptionMedDTO ePrescriptionMedToEPrescriptionMedDTO) {
        this.ePrescriptionService = ePrescriptionService;
        this.ePrescriptionToEPrescriptionDTO = ePrescriptionToEPrescriptionDTO;
        this.ePrescriptionMedToEPrescriptionMedDTO = ePrescriptionMedToEPrescriptionMedDTO;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EPrescriptionDTO>> getEPrescriptions() {
        return new ResponseEntity<>(ePrescriptionToEPrescriptionDTO.convert(ePrescriptionService.findAll()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EPrescriptionDTO> getOneEPrescription(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(ePrescriptionToEPrescriptionDTO.convert(ePrescriptionService.findOne(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EPrescriptionMedicineDTO>> getEPrescriptionMedicines(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(ePrescriptionMedToEPrescriptionMedDTO.convert(ePrescriptionService.findMedicinesForEPrescription(id)), HttpStatus.OK);
    }
}
