package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.ratings.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("select rating from PharmacyRating rating where rating.patient.id = ?1 and rating.pharmacy.id = ?2")
    Rating findRatingByPatientAndPharmacy(Integer patientId, Integer pharmacyId);

    @Query("select rating from MedicineRating rating where rating.patient.id = ?1 and rating.medicine.id = ?2")
    Rating findRatingByPatientAndMedicine(Integer patientId, Integer medicineId);

    @Query("select rating from MedicalWorkerRating rating where rating.patient.id = ?1 and rating.medicalWorker.id = ?2")
    Rating findRatingByPatientAndMedicalWorker(Integer patientId, Integer medicalWorkerId);

    @Query("select avg(rating.value) from PharmacyRating rating where rating.pharmacy.id = ?1")
    Double getAverageRatingForPharmacy(Integer pharmacyId);

    @Query("select avg(rating.value) from MedicineRating rating where rating.medicine.id = ?1")
    Double getAverageRatingForMedicine(Integer medicineId);

    @Query("select avg(rating.value) from MedicalWorkerRating rating where rating.medicalWorker.id = ?1")
    Double getAverageRatingForMedicalWorker(Integer medicalWorkerId);
}
