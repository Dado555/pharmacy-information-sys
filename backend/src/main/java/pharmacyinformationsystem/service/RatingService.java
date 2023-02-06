package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.ratings.Rating;

public interface RatingService extends GenericService<Rating> {

    // ---- FIND BY ENTITY
    Rating findRatingByPatientAndPharmacy(Integer patientId, Integer pharmacyId);

    Rating findRatingByPatientAndMedicine(Integer patientId, Integer medicineId);

    Rating findRatingByPatientAndMedicalWorker(Integer patientId, Integer medicalWorkerId);

    // ---- CALCULATE AVERAGE RATING FOR ENTITY
    Double calculateAverageRatingForPharmacy(Integer entityId);

    Double calculateAverageRatingForMedicine(Integer entityId);

    Double calculateAverageRatingForMedicalWorker(Integer medicalWorkerId);

    // ---- IS ENTITY RATEABLE
    Boolean isPharmacyRateable(Integer pharmacyId, Integer patientId);
    Boolean isMedicineRateable(Integer medicineId, Integer patientId);
    Boolean isPharmacistRateable(Integer pharmacist, Integer patientId);
    Boolean isDermatologistRateable(Integer dermatologistId, Integer patientId);

        // ---- ADD ENTITY RATING
    Rating tryAddPharmacyRating(Integer patientId, Integer pharmacyId, Integer value);
    Rating addPharmacyRating(Integer patientId, Integer pharmacyId, Integer value);

    Rating tryAddMedicineRating(Integer patientId, Integer medicineId, Integer value);
    Rating addMedicineRating(Integer patientId, Integer medicineId, Integer value);

    Rating addMedicalWorkerRating(Integer patientId, Integer medicalWorkerId, Integer value);
}


