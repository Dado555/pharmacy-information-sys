package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.ratings.MedicineRating;
import pharmacyinformationsystem.model.ratings.PharmacyRating;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.repository.RatingRepository;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class JpaRatingService implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    private final RatingRepository ratingRepository;

    private final PatientService patientService;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineService medicineService;
    private final PharmacistService pharmacistService;
    private final DermatologistService dermatologistService;

    @Autowired
    public JpaRatingService(RatingRepository ratingRepository, PatientService patientService,
                            PharmacyRepository pharmacyRepository, MedicineService medicineService, PharmacistService pharmacistService, DermatologistService dermatologistService) {
        this.ratingRepository = ratingRepository;
        this.patientService = patientService;
        this.pharmacyRepository = pharmacyRepository;
        this.medicineService = medicineService;
        this.pharmacistService = pharmacistService;
        this.dermatologistService = dermatologistService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rating> findAll() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Rating findOne(Integer id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Rating findRatingByPatientAndPharmacy(Integer patientId, Integer pharmacyId) {
        return ratingRepository.findRatingByPatientAndPharmacy(patientId, pharmacyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Rating findRatingByPatientAndMedicine(Integer patientId, Integer medicineId) {
        return ratingRepository.findRatingByPatientAndMedicine(patientId, medicineId);
    }

    @Override
    @Transactional(readOnly = true)
    public Rating findRatingByPatientAndMedicalWorker(Integer patientId, Integer medicalWorkerId) {
        return ratingRepository.findRatingByPatientAndMedicalWorker(patientId, medicalWorkerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRatingForPharmacy(Integer pharmacyId) {
        return ratingRepository.getAverageRatingForPharmacy(pharmacyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRatingForMedicine(Integer medicineId) {
        return ratingRepository.getAverageRatingForMedicine(medicineId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRatingForMedicalWorker(Integer medicalWorkerId) {
        return ratingRepository.getAverageRatingForMedicalWorker(medicalWorkerId);
    }

    @Transactional(readOnly = true)
    public Boolean isPharmacyRateable(Integer pharmacyId, Integer patientId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            return false;

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null)
            throw new NotFoundException("Pharmacy not found!");

        for (MedicineReservation mr: patient.getMedicineReservationList()) {
            if (mr.getStatus().equals(MedicineReservationStatus.TAKEN) && mr.getReservationItems().stream().anyMatch(item -> item.getPharmacyMedicine().getPharmacy().getId().equals(pharmacyId)))
                return true;
        }
        for (Appointment app: patient.getAppointments()) {
            if (app.getAppointmentStatus().equals(AppointmentStatus.DONE) && app.getPharmacy().getId().equals(pharmacyId))
                return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Boolean isMedicineRateable(Integer medicineId, Integer patientId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        for (MedicineReservation mr: patient.getMedicineReservationList()) {
            if (mr.getStatus().equals(MedicineReservationStatus.TAKEN) && mr.getReservationItems().stream().anyMatch(item -> item.getPharmacyMedicine().getMedicine().getId().equals(medicineId)))
                return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Boolean isPharmacistRateable(Integer pharamcistId, Integer patientId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Pharmacist pharmacist = pharmacistService.findOne(pharamcistId);
        if (pharmacist == null)
            throw new NotFoundException("Dermatologist not found!");

        return patient.getAppointments().stream().anyMatch(app -> app.getAppointmentStatus().equals(AppointmentStatus.DONE) && app.getMedicalWorker().getId().equals(pharamcistId));
    }

    @Transactional(readOnly = true)
    public Boolean isDermatologistRateable(Integer dermatologistId, Integer patientId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Dermatologist dermatologist = dermatologistService.findOne(dermatologistId);
        if (dermatologist == null)
            throw new NotFoundException("Dermatologist not found!");

        return patient.getAppointments().stream().anyMatch(app -> app.getAppointmentStatus().equals(AppointmentStatus.DONE) && app.getMedicalWorker().getId().equals(dermatologistId));
    }

    @Override
    public Rating tryAddPharmacyRating(Integer patientId, Integer pharmacyId, Integer value) {
        try {
            return addPharmacyRating(patientId, pharmacyId, value);
        } catch (Exception e) {
            throw new BadRequestException("Rating for pharmacy performed twice at the same time!");
        }
    }

    @Override
    public Rating addPharmacyRating(Integer patientId, Integer pharmacyId, Integer value) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Pharmacy pharmacy = entityManager.find(Pharmacy.class, pharmacyId);
        entityManager.lock(pharmacy, LockModeType.OPTIMISTIC);
        if (pharmacy == null)
            throw new NotFoundException("Pharmacy not found!");

        PharmacyRating rating = (PharmacyRating) findRatingByPatientAndPharmacy(patientId, pharmacyId);
        if (rating == null)
            rating = new PharmacyRating(value, patient, System.currentTimeMillis(), pharmacy);
        else {
            rating.setPharmacy(pharmacy);
            rating.setDateTime(System.currentTimeMillis());
            rating.setValue(value);
        }
        save(rating);
        pharmacy.setRating(calculateAverageRatingForPharmacy(pharmacyId));
        pharmacyRepository.save(pharmacy);
        return rating;
    }

    @Override
    public Rating tryAddMedicineRating(Integer patientId, Integer medicineId, Integer value) {
        try {
            return addMedicalWorkerRating(patientId, medicineId, value);
        } catch (Exception e) {
            throw new BadRequestException("Rating for medicine performed twice at the same time!");
        }
    }

    @Override
    public Rating addMedicineRating(Integer patientId, Integer medicineId, Integer value) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Medicine medicine = entityManager.find(Medicine.class, medicineId);
        entityManager.lock(medicine, LockModeType.OPTIMISTIC);
        if (medicine == null)
            throw new NotFoundException("Medicine not found!");

        MedicineRating rating = (MedicineRating) findRatingByPatientAndMedicine(patientId, medicineId);
        if (rating == null)
            rating = new MedicineRating(value, patient, System.currentTimeMillis(), medicine);
        else {
            rating.setMedicine(medicine);
            rating.setDateTime(System.currentTimeMillis());
            rating.setValue(value);
        }

        save(rating);
        medicine.setRating(calculateAverageRatingForMedicine(medicineId));
        medicineService.save(medicine);
        return rating;
    }

    @Override
    public Rating addMedicalWorkerRating(Integer patientId, Integer medicalWorkerId, Integer value) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");
        Pharmacist pharmacist = pharmacistService.findOne(medicalWorkerId);
        if (pharmacist == null) {
            Dermatologist dermatologist = dermatologistService.findOne(medicalWorkerId);
            if (dermatologist == null)
                throw new NotFoundException("Medical worker not found!");

            boolean rateable = false;
            for(Appointment a: patient.getAppointments()) {
                if (a.getMedicalWorker().getId().equals(dermatologist.getId())) {
                    rateable = true;
                    break;
                }
            }
            if (!rateable)
                throw new BadRequestException("Medical worker is not rateable!");

            MedicalWorkerRating rating = (MedicalWorkerRating) findRatingByPatientAndMedicalWorker(patientId, medicalWorkerId);
            if (rating == null)
                rating = new MedicalWorkerRating(value, patient, System.currentTimeMillis(), dermatologist);
            else {
                rating.setMedicalWorker(dermatologist);
                rating.setDateTime(System.currentTimeMillis());
                rating.setValue(value);
            }

            save(rating);
            dermatologist.setRating(calculateAverageRatingForMedicalWorker(medicalWorkerId));
            dermatologistService.save(dermatologist);
            return rating;
        }

        boolean rateable = false;
        for(Appointment a: patient.getAppointments()) {
            if (a.getMedicalWorker().getId().equals(pharmacist.getId())) {
                rateable = true;
                break;
            }
        }
        if (!rateable)
            throw new BadRequestException("Medical worker is not rateable!");

        MedicalWorkerRating rating = (MedicalWorkerRating) findRatingByPatientAndMedicalWorker(patientId, medicalWorkerId);
        if (rating == null)
            rating = new MedicalWorkerRating(value, patient, System.currentTimeMillis(), pharmacist);
        else {
            rating.setMedicalWorker(pharmacist);
            rating.setDateTime(System.currentTimeMillis());
            rating.setValue(value);
        }

        save(rating);
        pharmacist.setRating(calculateAverageRatingForMedicalWorker(medicalWorkerId));
        pharmacistService.save(pharmacist);
        return rating;
    }

    @Override
    public Rating save(Rating entity) {
        return ratingRepository.save(entity);
    }
}
