package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.MedicineReservationItem;
import pharmacyinformationsystem.service.MedicineReservationService;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.support.MedicineReservationItemToMedicineReservationItemDTO;
import pharmacyinformationsystem.web.dto.domain.MedicineReservationItemDTO;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/medicine-reservations")
public class MedicineReservationController {

    private final MedicineReservationService medicineReservationService;
    private final PatientService patientService;

    private final MedicineReservationItemToMedicineReservationItemDTO toDTO;

    @Autowired
    public MedicineReservationController(MedicineReservationService medicineReservationService, PatientService patientService, MedicineReservationItemToMedicineReservationItemDTO toDTO) {
        this.medicineReservationService = medicineReservationService;
        this.patientService = patientService;
        this.toDTO = toDTO;
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineReservationItemDTO>> getReservationItems(@RequestParam Integer id, @RequestParam Integer pharmacistId)  {
        return new ResponseEntity<>(toDTO.convert(medicineReservationService.getReservationItems(id, pharmacistId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACIST')")
    @PutMapping(value = "/medicine-issued", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> issueMedicine(@RequestBody Map<String, Integer> reservationItemId) {
        MedicineReservationItem item = medicineReservationService.issueReservationItem(reservationItemId.get("id"));
        patientService.addPointsToPatient(item.getMedicineReservation().getPatient().getId(), item.getPharmacyMedicine().getMedicine().getPoints());
        medicineReservationService.sendIssueConfirmationEmail(item, item.getPharmacyMedicine().getMedicine().getPoints());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}
