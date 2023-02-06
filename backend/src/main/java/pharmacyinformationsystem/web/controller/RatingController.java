package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.service.RatingService;
import pharmacyinformationsystem.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/api/ratings")
public class RatingController {

    private final UserService userService;
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @GetMapping(value = "/pharmacy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageRatingForPharmacy(@PathVariable Integer id) {
            return new ResponseEntity<>(ratingService.calculateAverageRatingForPharmacy(id), HttpStatus.OK);
    }

    @GetMapping(value = "/medicine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageRatingForMedicine(@PathVariable Integer id) {
        return new ResponseEntity<>(ratingService.calculateAverageRatingForMedicine(id), HttpStatus.OK);
    }

    @GetMapping(value = "/medical-worker/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getAverageRatingForMedicalWorker(@PathVariable Integer id) {
        return new ResponseEntity<>(ratingService.calculateAverageRatingForMedicalWorker(id), HttpStatus.OK);
    }

    @GetMapping(value = "/pharmacy/{id}/patient-rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getAverageRatingForPharmacyAndPatient(@PathVariable Integer id) {
        Rating rating = ratingService.findRatingByPatientAndPharmacy(userService.getAuthenticatedUser().getId(), id);
        if (rating == null)
            return null;

        return new ResponseEntity<>(rating.getValue(), HttpStatus.OK);
    }

    @GetMapping(value = "/medicine/{id}/patient-rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getAverageRatingForMedicineAndPatient(@PathVariable Integer id) {
        return new ResponseEntity<>(ratingService.findRatingByPatientAndMedicine(userService.getAuthenticatedUser().getId(), id).getValue(), HttpStatus.OK);
    }

    @GetMapping(value = "/medical-worker/{id}/patient-rating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getAverageRatingForMedicalWorkerAndPatient(@PathVariable Integer id) {
        return new ResponseEntity<>(ratingService.findRatingByPatientAndMedicalWorker(userService.getAuthenticatedUser().getId(), id).getValue(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/pharmacy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addPharmacyRating(@PathVariable Integer id, @RequestParam Integer value) {
        return new ResponseEntity<>(ratingService.tryAddPharmacyRating(userService.getAuthenticatedUser().getId(), id, value).getValue(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/medicine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addMedicineRating(@PathVariable Integer id, @RequestParam Integer value) {
        return new ResponseEntity<>(ratingService.tryAddMedicineRating(userService.getAuthenticatedUser().getId(), id, value).getValue(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/medical-worker/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addMedicalWorkerRating(@PathVariable Integer id, @RequestParam Integer value) {
        return new ResponseEntity<>(ratingService.addMedicalWorkerRating(userService.getAuthenticatedUser().getId(), id, value).getValue(), HttpStatus.OK);
    }
}