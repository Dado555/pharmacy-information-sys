package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.repository.ConfirmationTokenRepository;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.*;

import pharmacyinformationsystem.web.dto.domain.*;
import pharmacyinformationsystem.web.dto.searchparameters.EPrescriptionSearchParameters;
import pharmacyinformationsystem.web.dto.users.PatientDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final EPrescriptionService ePrescriptionService;
    private final QRCodeResultService qrCodeResultService;
    private final SubscriptionService subscriptionService;
    private final PatientToPatientDTO toPatientDTO;
    private final MedicineToMedicineDTO toMedicineDTO;
    private final MedicineReservationToMedicineReservationDTO medResToMedResDTO;
    private final MedicineReservationDTOToMedicineReservation medResDTOtoMedRes;
    private final EPrescriptionToEPrescriptionDTO toEPrescriptionDTO;
    private final SubscriptionToSubscriptionDTO toSubscriptionDTO;
    private final ComplaintToComplaintDTO toComplaintDTO;
    private final QRCodeResultToQRCodeResultDTO toQRCodeResultDTO;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AppointmentToAppointmentDTO toAppointmentDTO;
    private final AppointmentService appointmentService;

    @Autowired
    public PatientController(PatientService patientService,
                             EPrescriptionService ePrescriptionService, QRCodeResultService qrCodeResultService,
                             PatientToPatientDTO toPatientDTO, MedicineToMedicineDTO toMedicineDTO,
                             MedicineReservationToMedicineReservationDTO medResToMedResDTO,
                             MedicineReservationDTOToMedicineReservation medResDTOtoMedRes,
                             EPrescriptionToEPrescriptionDTO toEPrescriptionDTO,
                             ConfirmationTokenRepository confirmationTokenRepository,
                             SubscriptionService subscriptionService,
                             SubscriptionToSubscriptionDTO toSubscriptionDTO,
                             ComplaintToComplaintDTO toComplaintDTO,
                             QRCodeResultToQRCodeResultDTO toQRCodeResultDTO,
                             AppointmentToAppointmentDTO toAppointmentDTO, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.ePrescriptionService = ePrescriptionService;
        this.qrCodeResultService = qrCodeResultService;
        this.toPatientDTO = toPatientDTO;
        this.toMedicineDTO = toMedicineDTO;
        this.medResToMedResDTO = medResToMedResDTO;
        this.medResDTOtoMedRes = medResDTOtoMedRes;
        this.toEPrescriptionDTO = toEPrescriptionDTO;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.subscriptionService = subscriptionService;
        this.toSubscriptionDTO = toSubscriptionDTO;
        this.toComplaintDTO = toComplaintDTO;
        this.toQRCodeResultDTO = toQRCodeResultDTO;
        this.toAppointmentDTO = toAppointmentDTO;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'DERMATOLOGIST', 'PHARMACIST')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PatientDTO>> getPatients(@RequestParam String input, @RequestParam Integer id) throws UnsupportedEncodingException {
        String in = URLDecoder.decode(input, "UTF-8");
        return new ResponseEntity<>(patientService.findPatientsByFirstAndLastName(in, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(value = "/set-appointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentService.findOne(appointmentDTO.getId());
        Patient patient = patientService.findOne(appointmentDTO.getPatientId());
        if (appointment == null || patient == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
        appointment.setPatient(patient);
        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.saveWithLock(appointment)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> getOnePatient(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toPatientDTO.convert(patientService.findOne(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/allergic-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineDTO>> getAllergicMedicines(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toMedicineDTO.convert(patientService.findAllergicMedicinesForPatient(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/reserved-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineReservationDTO>> getReservedMedicines(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(medResToMedResDTO.convert(patientService.findReservedMedicinesForPatient(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/e-prescriptions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EPrescriptionDTO>> getEPrescriptions(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toEPrescriptionDTO.convert(patientService.findEPrescriptionsForPatient(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/complaints", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ComplaintDTO>> getComplaints(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(toComplaintDTO.convert(patientService.findComplaintsForPatient(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/confirm-registration/{token}")
    public RedirectView confirmPatient(@PathVariable("token") String token) {
        ConfirmationToken token2 = confirmationTokenRepository.findByConfirmationToken(token);
        if (token != null) {
            Patient patient = patientService.findOne(token2.getDataId());
            patient.setActive(true);
            patientService.save(patient);
            RedirectView redirect = new RedirectView();
            redirect.setUrl("https://pis-front.herokuapp.com/login");
            return redirect;
        }
        return null;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> createPatient(@RequestBody Patient patient) {
        return new ResponseEntity<>(toPatientDTO.convert(patientService.createPatient(patient)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody Patient patient, @PathVariable Integer id) {
        Patient p = patientService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        p.update(patient.getFirstName(), patient.getLastName(), patient.getPhoneNumber());
        return new ResponseEntity<>(toPatientDTO.convert(patientService.save(p)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable("id") Integer id) {
        Patient patient = patientService.findOne(id);
        if (patient == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        patient.setActive(false);
        return new ResponseEntity<>(toPatientDTO.convert(patient), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/is-sub-pharmacy={pharId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isSubInPharmacy(@PathVariable("id") Integer id, @PathVariable("pharId") Integer pharId) {
        return new ResponseEntity<>(subscriptionService.findSubscription(id, pharId) != null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{id}/subscribe-pharmacy={pharId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> subscribeToPharmacy(@PathVariable("id") Integer id, @PathVariable("pharId") Integer pharId) {
        return new ResponseEntity<>(patientService.subscribeToPharmacy(id, pharId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @DeleteMapping(value = "/{id}/unsubscribe-pharmacy={pharId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> unsubscribeToPharmacy(@PathVariable("id") Integer id, @PathVariable("pharId") Integer pharId) {
        return new ResponseEntity<>(patientService.unsubscribeToPharmacy(id, pharId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{id}/allergic-medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineDTO>> createAllergicMedicine(@PathVariable("id") Integer id, @RequestBody List<Integer> medicineIds) {
        return new ResponseEntity<>(toMedicineDTO.convert(patientService.addAllergicMedicinesForPatient(id, medicineIds)), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/penalty", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PatientDTO> addPenalty(@RequestBody Map<String, Integer> patientsAppointment, @PathVariable Integer id) {
        return new ResponseEntity<>(toPatientDTO.convert(patientService.setPenalty(patientsAppointment, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{id}/reserved-medicines", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicineReservationDTO> reserveMedicine(@PathVariable("id") Integer id, @RequestBody MedicineReservationDTO reservation) {
        MedicineReservation medicineReservation = medResDTOtoMedRes.convert(reservation);
        if (medicineReservation == null)
            throw new BadRequestException("Medicine reservation bad request");

        patientService.addMedicineReservationForPatient(id, medicineReservation);
        patientService.sendConfirmationMail(medicineReservation.getPatient(), medicineReservation.getId());
        return new ResponseEntity<>(medResToMedResDTO.convert(medicineReservation), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping(value = "/{id}/reserved-medicines/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> cancelReservedMedicine(@PathVariable("id") Integer id, @PathVariable("reservationId") Integer reservationId) {
        patientService.cancelMedicineReservationForPatient(id, reservationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{id}/e-prescriptions/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EPrescriptionDTO>> searchEPrescriptions(@PathVariable("id") Integer id, @RequestBody EPrescriptionSearchParameters searchParameters) {
        if (patientService.findOne(id) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(toEPrescriptionDTO.convert(ePrescriptionService.searchByCriteria(id, searchParameters)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/allergic-medicines")
    public ResponseEntity<List<MedicineDTO>> deleteAllergicMedicine(@PathVariable("id") Integer id, @RequestParam("medicineId") Integer medicineId) {
        return new ResponseEntity<>(toMedicineDTO.convert(patientService.deleteAllergicMedicineForPatient(id, medicineId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{id}/complaints", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComplaintDTO> addComplaint(@PathVariable("id") Integer id, @RequestBody ComplaintDTO complaintDTO) {
        patientService.addComplaint(id, new Complaint(complaintDTO.getComplaintMessage(), complaintDTO.getEntityId().toString()));
        return new ResponseEntity<>(new ComplaintDTO(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{patientId}/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubscriptionDTO>> subscriptions(@PathVariable("patientId") Integer patientId) {
        return new ResponseEntity<>(toSubscriptionDTO.convert(subscriptionService.findSubsByPatientId(patientId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{patientId}/upload_qrcode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QRCodeResultDTO>> getMedicinesFromQR(@RequestBody String data, @PathVariable("patientId") Integer patientId) {
        return new ResponseEntity<>(toQRCodeResultDTO.convert(qrCodeResultService.getMedicinesFromQR(data, patientId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping(value = "/{patientId}/buy_after_qrcode/{qrCodeResultId}")
    public ResponseEntity<Boolean> buyMedicinesAfterQR(@PathVariable("patientId") Integer patientId, @PathVariable("qrCodeResultId") Integer qrCodeResultId) {
        return new ResponseEntity<>(qrCodeResultService.buyMedicinesAfterQR(patientId, qrCodeResultId), HttpStatus.OK);
    }
}