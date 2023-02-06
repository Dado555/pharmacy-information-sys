package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.EPrescription;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Penalty;
import pharmacyinformationsystem.web.dto.users.PatientDTO;
import pharmacyinformationsystem.web.dto.users.PenaltyDTO;

import java.util.List;
import java.util.Map;

public interface PatientService extends GenericService<Patient> {

    Patient findOneWithLock(Integer id);

    Patient findPatientByEmail(String email);

    List<PatientDTO> findPatientsByFirstAndLastName(String firstAndLastName, Integer medicalWorkerId);

    List<Medicine> findAllergicMedicinesForPatient(Integer patientId);

    List<MedicineReservation> findReservedMedicinesForPatient(Integer patientId);

    List<EPrescription> findEPrescriptionsForPatient(Integer patientId);

    List<Complaint> findComplaintsForPatient(Integer patienId);

    List<Medicine> addAllergicMedicinesForPatient(Integer patientId, List<Integer> medicineIds);

    List<Medicine> deleteAllergicMedicineForPatient(Integer patientId, Integer medicineId);

    MedicineReservation addMedicineReservationForPatient(Integer patientId, MedicineReservation reservation);

    Patient cancelMedicineReservationForPatient(Integer patientId, Integer reservationId);

    Patient setPenalty(Map<String, Integer> patientsAppointment, Integer patientId);

    void sendConfirmationMail(Patient patient, Integer id);

    Patient deleteById(Integer id);

    Boolean unsubscribeToPharmacy(Integer id, Integer pharId);

    Boolean subscribeToPharmacy(Integer id, Integer pharId);

    Patient createPatient(Patient patient);

    Patient addPointsToPatient(Integer id, Double points);

    Complaint addComplaint(Integer id, Complaint complaint);
}