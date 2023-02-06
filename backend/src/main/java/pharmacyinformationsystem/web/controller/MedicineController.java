package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.service.MedicineService;
import pharmacyinformationsystem.service.PharmacyMedicineService;
import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.support.MedicineToMedicineDTO;
import pharmacyinformationsystem.support.PharmacyMedicineToPharmacyMedicineDTO;
import pharmacyinformationsystem.web.dto.*;
import pharmacyinformationsystem.web.dto.domain.MedicineDTO;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;
import pharmacyinformationsystem.web.dto.pages.MedicinePageDTO;
import pharmacyinformationsystem.web.dto.pages.PharmacyMedicinePage;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;
    private final MedicineToMedicineDTO medicineToMedicineDTO;
    private final PharmacyMedicineService pharmacyMedicineService;
    private final PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO;
    private final PharmacyService pharmacyService;

    @Autowired
    public MedicineController(MedicineService medicineService, MedicineToMedicineDTO medicineToMedicineDTO, PharmacyMedicineService pharmacyMedicineService,
                              PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO,
                              PharmacyService pharmacyService) {
        this.medicineService = medicineService;
        this.medicineToMedicineDTO = medicineToMedicineDTO;
        this.pharmacyMedicineService = pharmacyMedicineService;
        this.pharmacyMedicineToPharmacyMedicineDTO = pharmacyMedicineToPharmacyMedicineDTO;
        this.pharmacyService = pharmacyService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicinePageDTO> getMedicines(@RequestParam Optional<Integer> page) {
        Page<Medicine> medicines = medicineService.findAll(page.orElse(0));
        return new ResponseEntity<>(new MedicinePageDTO(medicineToMedicineDTO.convert(medicines.toList()), medicines.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> getOneMedicine(@PathVariable("id") Integer id) {
        Medicine medicine = medicineService.findOne(id);
        if (medicine == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(medicineToMedicineDTO.convert(medicine), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageRating(@PathVariable("id") Integer id) {
        Medicine medicine = medicineService.findOne(id);
        if (medicine == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(medicine.getRating(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/pharmacy/{id}/can-add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineDTO>> getUnregisteredMedicines(@PathVariable("id") Integer pharmacyId) {
        List<Medicine> medicines = new ArrayList<>(medicineService.findUnregisteredForPharmacy(pharmacyId));
        return new ResponseEntity<>(medicineToMedicineDTO.convert(medicines), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PostMapping(value = "/register-pharmacy={pharId}-medicine={medId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyMedicineDTO> registerMedicine(@PathVariable("pharId") Integer pharmacyId, @PathVariable("medId") Integer medicineId) {
        Pharmacy pharmacy = pharmacyService.findOne(pharmacyId);
        Medicine medicine = medicineService.findOne(medicineId);
        if(pharmacy == null || medicine == null)
            throw new NotFoundException("Pharmacy or medicine not found!");
        PharmacyMedicine phMedicine = new PharmacyMedicine();
        phMedicine.setMedicine(medicine);
        phMedicine.setPharmacy(pharmacy);
        phMedicine.setRating(0.0);
        phMedicine.setAvailableAmount(0);
        phMedicine.setPrice(0.0);
        pharmacyMedicineService.save(phMedicine);
        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(phMedicine), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> createMedicine(@RequestBody Medicine medicine) {
        Medicine savedMedicine = medicineService.save(medicine);
        savedMedicine.setRating((double)0);
        return new ResponseEntity<>(medicineToMedicineDTO.convert(savedMedicine), HttpStatus.OK);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineDTO> updateMedicine(@RequestBody Medicine medicine, @PathVariable Integer id) {
        Medicine m = medicineService.findOne(id);
        if (m == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        m.update(medicine);
        return new ResponseEntity<>(medicineToMedicineDTO.convert(m), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MedicineDTO> deleteMedicine(@PathVariable("id") Integer id) {
        // TODO: Logicko brisanje?
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicinePageDTO> searchMedicines(@RequestParam Optional<Integer> page, @RequestBody MedicineSearchParameters searchParameters) {
        Page<Medicine> medicines = medicineService.searchByCriteria(searchParameters, Optional.of(page.orElse(0)));
        return new ResponseEntity<>(new MedicinePageDTO(medicineToMedicineDTO.convert(medicines.toList()), medicines.getTotalPages()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'PHARMACY_ADMIN')")
    @PostMapping(value = "/pharmacy-search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyMedicinePage> searchPharmacyMedicines(@RequestParam Optional<Integer> page, @RequestBody MedicineSearchParameters searchParameters) {
        Page<PharmacyMedicine> medicines = pharmacyMedicineService.searchByCriteria(searchParameters, Optional.of(page.orElse(0)));
        return new ResponseEntity<>(new PharmacyMedicinePage(pharmacyMedicineToPharmacyMedicineDTO.convert((List<PharmacyMedicine>)(List<?>)medicines.toList()), medicines.getTotalPages()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @DeleteMapping(value = "/pharmacy{pharmacyId}/medicine{medicineId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyMedicineDTO> deletePharmacyMedicine(@PathVariable("pharmacyId") Integer pharmacyId, @PathVariable("medicineId") Integer medicineId) {
        PharmacyMedicine medicine = pharmacyMedicineService.findOne(medicineId);
        if(medicine == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        pharmacyMedicineService.deleteByMedicineIdAndPharmacyId(medicineId, pharmacyId);
        return new ResponseEntity<>(pharmacyMedicineToPharmacyMedicineDTO.convert(medicine), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/pharmacy-medicine", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updatePharmacyMedicine(@RequestBody PharmacyMedicineEdit medicineEdit) {
        System.out.println(" ***** ID: " + medicineEdit.getId());
        PharmacyMedicine pharmed = pharmacyMedicineService.findOne(medicineEdit.getId());
        if (pharmed == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        pharmacyMedicineService.updatePharmacyMedicine(medicineEdit);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{pharmacyId}/pharmacy-medicine/action-price", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> setMedicineActionPrice(@RequestBody PharmacyMedicineDTO medicineEdit, @PathVariable("pharmacyId") Integer pharmacyId) {
        PharmacyMedicine pharmed = pharmacyMedicineService.findOne(medicineEdit.getId());
        Pharmacy pharmacy = pharmacyService.findOne(pharmacyId);
        if (pharmed == null || pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        pharmacyMedicineService.setActionPrice(pharmacyId, medicineEdit);
        pharmacyMedicineService.promotionMail(medicineEdit, pharmacyMedicineService.findOne(medicineEdit.getId()),
                 pharmacyService.getSubs(pharmacyId), pharmacy.getName());
        //pharmacyMedicineService.sendNewPromotionMail(pharmacyId, medicineEdit, pharmacyMedicineService.findOne(medicineEdit.getId()));
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/pharmacy-medicine={id}/sold-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getSoldMonthly(@PathVariable("id") Integer id) {
        PharmacyMedicine medicine = pharmacyMedicineService.findOne(id);
        if(medicine == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> sold = medicineToMedicineDTO.soldMonthly(medicine.getReservationItems());
        return new ResponseEntity<>(pharmacyMedicineService.medicinesSoldMonthly(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/pharmacy-medicine={id}/sold-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> getSoldYearly(@PathVariable("id") Integer id) {
        PharmacyMedicine medicine = pharmacyMedicineService.findOne(id);
        if(medicine == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        //List<Integer> sold = medicineToMedicineDTO.soldYearly(medicine.getReservationItems());
        return new ResponseEntity<>(pharmacyMedicineService.medicinesSoldYearly(id), HttpStatus.OK);
    }
}
