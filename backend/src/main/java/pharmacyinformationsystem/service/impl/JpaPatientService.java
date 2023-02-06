package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Penalty;
import pharmacyinformationsystem.repository.*;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.AppointmentToAppointmentDTO;
import pharmacyinformationsystem.support.PatientToPatientDTO;
import pharmacyinformationsystem.web.dto.users.PatientDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;


@Service
@Transactional
public class JpaPatientService implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientCategoryRepository patientCategoryRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PharmacyMedicineRepository pharmacyMedicineRepository;

    private final MedicineReservationService medicineReservationService;
    private final MedicineService medicineService;
    private final PharmacyService pharmacyService;
    private final SubscriptionService subscriptionService;
    private final AddressService addressService;
    private final RoleService roleService;
    private final SystemAdminService systemAdminService;
    private final PharmacyAdminService pharmacyAdminService;
    private final DermatologistService dermatologistService;
    private final SupplierService supplierService;

    private final PatientToPatientDTO toPatientDTO;
    private final AppointmentToAppointmentDTO toAppointmentDTO;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public JpaPatientService(PatientRepository patientRepository, PatientCategoryRepository patientCategoryRepository, ConfirmationTokenRepository confirmationTokenRepository, PharmacyMedicineRepository pharmacyMedicineRepository, MedicineReservationService medicineReservationService, MedicineService medicineService, AppointmentRepository appointmentRepository, PharmacyService pharmacyService, SubscriptionService subscriptionService, AddressService addressService, RoleService roleService, SystemAdminService systemAdminService, PharmacyAdminService pharmacyAdminService, DermatologistService dermatologistService, SupplierService supplierService, PatientToPatientDTO toPatientDTO, AppointmentToAppointmentDTO toAppointmentDTO, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.patientCategoryRepository = patientCategoryRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.medicineReservationService = medicineReservationService;
        this.pharmacyMedicineRepository = pharmacyMedicineRepository;
        this.medicineService = medicineService;
        this.appointmentRepository = appointmentRepository;
        this.pharmacyService = pharmacyService;
        this.subscriptionService = subscriptionService;
        this.addressService = addressService;
        this.roleService = roleService;
        this.systemAdminService = systemAdminService;
        this.pharmacyAdminService = pharmacyAdminService;
        this.dermatologistService = dermatologistService;
        this.supplierService = supplierService;
        this.toPatientDTO = toPatientDTO;
        this.toAppointmentDTO = toAppointmentDTO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Patient findOne(Integer id) {
        return patientRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Patient findOneWithLock(Integer id) {
        return patientRepository.findByIdWithLock(id);
    }

    @Override
    public Patient findPatientByEmail(String email) {
        return patientRepository.findByEmailAndActiveTrue(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO> findPatientsByFirstAndLastName(String firstAndLastName, Integer medicalWorkerId) {
        Comparator<Appointment> compareByStartDate = Comparator.comparing(Appointment::getStartDateAndTime);
        List<Patient> patients = patientRepository.findPatientsByFirstAndLastName(firstAndLastName);
        List<PatientDTO> patientDTOList = new ArrayList<>();
        for (Patient p : patients) {
            p.getAppointments().sort(compareByStartDate);
            PatientDTO patientDTO = toPatientDTO.convert(p);
            Long currentDate = System.currentTimeMillis();
            for (Appointment appointment : p.getAppointments()) {
                if ((appointment.getAppointmentStatus() == AppointmentStatus.RESERVED || appointment.getAppointmentStatus() == AppointmentStatus.CANCELED)
                        && appointment.getMedicalWorker().getId().equals(medicalWorkerId) && appointment.getStartDateAndTime() >= currentDate && appointment.getStartDateAndTime() <= currentDate + 86400000) {
                    patientDTO.setAppointment(toAppointmentDTO.convert(appointment));
                    break;
                }
            }
            patientDTOList.add(patientDTO);
        }
        return patientDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findAllergicMedicinesForPatient(Integer id) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        return new ArrayList<>(patient.getAllergicMedicines());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineReservation> findReservedMedicinesForPatient(Integer id) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        return new ArrayList<>(patient.getMedicineReservationList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EPrescription> findEPrescriptionsForPatient(Integer id) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        return new ArrayList<>(patient.getEPrescriptions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Complaint> findComplaintsForPatient(Integer id) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        return new ArrayList<>(patient.getComplaintList());
    }

    @Override
    public List<Medicine> addAllergicMedicinesForPatient(Integer id, List<Integer> medicineIds) {
        Patient patient = findOne(id);
        if (patient == null)
            throw  new NotFoundException("Patient not found!");

        for (Integer medicineId: medicineIds) {
            Medicine medicine = medicineService.findOne(medicineId);
            if (medicine == null || patient.getAllergicMedicines().contains(medicine))
                continue;
            patient.addAllergicMedicine(medicine);
        }
        save(patient);
        return new ArrayList<>(patient.getAllergicMedicines());
    }

    @Override
    public List<Medicine> deleteAllergicMedicineForPatient(Integer patientId, Integer medicineId) {
        Patient patient = findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Medicine medicine = medicineService.findOne(medicineId);
        if (medicine == null)
            throw new NotFoundException("Medicine not found!");

        patient.getAllergicMedicines().remove(medicine);
        save(patient);
        return new ArrayList<>(patient.getAllergicMedicines());
    }

    @Override
    public Complaint addComplaint(Integer id, Complaint complaint) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        complaint.setPatient(patient);
        patient.getComplaintList().add(complaint);
        save(patient);
        return complaint;
    }

    @Override
    public Patient addPointsToPatient(Integer id, Double points) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        patient.setPoints(patient.getPoints() + points);

        List<PatientCategory> categories = patientCategoryRepository.findAll();
        categories.sort(Comparator.comparing(PatientCategory::getMinimumPoints).reversed());
        for (PatientCategory c: categories) {
            if (patient.getPoints() > c.getMinimumPoints()) {
                patient.setPatientCategory(c);
                break;
            }
        }
        save(patient);
        return patient;
    }

    @Override
    public MedicineReservation addMedicineReservationForPatient(Integer id, MedicineReservation reservation) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        if (patient.getPenalties().size() >= 3)
            throw new BadRequestException("Patient with 3 or more penalties is not allowed to reserve a medicine!");

        if ((reservation.getDateAndTime() - System.currentTimeMillis()) < 86400000)
            throw new BadRequestException("Invalid date!");

        try {
            for (MedicineReservationItem item: reservation.getReservationItems()) {
                if (item.getPharmacyMedicine().getAvailableAmount() < item.getAmount())
                    throw new BadRequestException("Not enough items in stock to be reserved!");
                if (patient.getAllergicMedicines().stream().anyMatch(med -> med.getId().equals(item.getPharmacyMedicine().getMedicine().getId())))
                    throw new BadRequestException("Patient is allergic to selected medicine");

                PharmacyMedicine pharmacyMedicine = pharmacyMedicineRepository.findById(item.getPharmacyMedicine().getId()).orElse(null);
                if (pharmacyMedicine == null)
                    throw new NotFoundException("Medicine not found in pharmacy!");

                pharmacyMedicine.setAvailableAmount(item.getPharmacyMedicine().getAvailableAmount() - item.getAmount());
                item.setDiscount(patient.getPatientCategory().getDiscount() / 100D * pharmacyMedicine.getPrice());
                pharmacyMedicineRepository.save(pharmacyMedicine);
            }

            reservation.setStatus(MedicineReservationStatus.RESERVED);
            patient.addMedicineReservation(reservation);
            medicineReservationService.save(reservation);
            return reservation;
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new BadRequestException("Someone already reserved this item. Try again.");
        }
    }

    @Override
    public Patient cancelMedicineReservationForPatient(Integer patientId, Integer reservationId) {
        Patient patient = findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        MedicineReservation reservation = medicineReservationService.findOne(reservationId);
        if (reservation == null)
            throw new NotFoundException("Medicine Reservation not found!");

        if ((reservation.getDateAndTime() - System.currentTimeMillis()) <= 86400)
            throw new BadRequestException("Medicine reservation can only be canceled 24 hours before the reservation date!");

        for (MedicineReservationItem item: reservation.getReservationItems())
            item.getPharmacyMedicine().setAvailableAmount(item.getPharmacyMedicine().getAvailableAmount() + item.getAmount());

        reservation.getReservationItems().clear();
        reservation.setStatus(MedicineReservationStatus.CANCELED);
        try { medicineReservationService.save(reservation); }
        catch (ObjectOptimisticLockingFailureException e) { throw new BadRequestException("Someone canceled this medicine at the same time. Please, try again."); }
        return patient;
    }

    @Override
    public Patient setPenalty(Map<String, Integer> patientsAppointment, Integer patientId) {
        Patient patient = findOne(patientId);
        Appointment appointment = appointmentRepository.findAppointmentById(patientsAppointment.get("appointmentId"));
        appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
        if (patient == null) {
            throw new NotFoundException("Patient not found!");
        }
        Penalty penalty = new Penalty(System.currentTimeMillis(), "Patient did not appear for the appointment.", patient);
        patient.getPenalties().add(penalty);
        return save(patient);
    }

    @Async
    @Transactional(propagation= Propagation.NEVER)
    @Override
    public void sendConfirmationMail(Patient patient, Integer id) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        String recipient = patient.getEmail();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Medicine reservation confirmation");
            String content = MailConstants.getMailMessage("Medicine reservation ID: " + id, "Your medicine reservation is confirmed!");
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient deleteById(Integer id) {
        Patient patient = findOne(id);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        patient.setActive(false);
        save(patient);
        return patient;
    }

    @Override
    public Boolean unsubscribeToPharmacy(Integer id, Integer pharId) {
        Patient patient = findOne(id);
        Pharmacy pharmacy = pharmacyService.findOne(pharId);
        if(patient == null || pharmacy == null)
            return false;
        subscriptionService.deleteSubscription(id, pharId);
        return true;
    }

    @Override
    public Boolean subscribeToPharmacy(Integer id, Integer pharId) {
        Patient patient = findOne(id);
        Pharmacy pharmacy = pharmacyService.findOne(pharId);
        if(patient == null || pharmacy == null)
            return false;
        Subscription sub = new Subscription();
        sub.setPharmacy(pharmacy);
        sub.setPatient(patient);
        subscriptionService.save(sub);
        return true;
    }

    @Override
    public Patient createPatient(Patient patient) {
        if (patientRepository.findByEmailAndActiveTrueWithLock(patient.getEmail()) != null
        || systemAdminService.findSystemAdminByEmail(patient.getEmail()) != null
        || pharmacyAdminService.findPharmacyAdminByEmail(patient.getEmail()) != null
        || dermatologistService.findDermatologistByEmail(patient.getEmail()) != null
        || supplierService.findSupplierByEmail(patient.getEmail()) != null) {
            return null;
        }

        Address address = addressService.save(patient.getAddress());
        patient.setAddress(address);
        patient.setActive(false);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(this.roleService.findByName("ROLE_PATIENT"));
        patient.setPoints(0.0);
        List<Penalty> penalties = new ArrayList<Penalty>() {};
        patient.setPenalties(penalties);
        Patient savedPatient = save(patient);

        if (savedPatient == null)
            return null;

        String myToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(patient.getId(), myToken);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        String recipient = patient.getEmail();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Complete registration");
            String poruka = "<!doctype html>\n" +
                    "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                    "  <head>\n" +
                    "    <title>\n" +
                    "    </title>\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "    <style type=\"text/css\">\n" +
                    "      #outlook a{padding: 0;}\n" +
                    "      \t\t\t.ReadMsgBody{width: 100%;}\n" +
                    "      \t\t\t.ExternalClass{width: 100%;}\n" +
                    "      \t\t\t.ExternalClass *{line-height: 100%;}\n" +
                    "      \t\t\tbody{margin: 0; padding: 0; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;}\n" +
                    "      \t\t\ttable, td{border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt;}\n" +
                    "      \t\t\timg{border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; -ms-interpolation-mode: bicubic;}\n" +
                    "      \t\t\tp{display: block; margin: 13px 0;}\n" +
                    "    </style>\n" +
                    "    <style type=\"text/css\">\n" +
                    "      @media only screen and (max-width:480px) {\n" +
                    "      \t\t\t  \t\t@-ms-viewport {width: 320px;}\n" +
                    "      \t\t\t  \t\t@viewport {\twidth: 320px; }\n" +
                    "      \t\t\t\t}\n" +
                    "    </style>\n" +
                    "    <style type=\"text/css\">\n" +
                    "      @media only screen and (max-width:480px) {\n" +
                    "      \n" +
                    "      \t\t\t  table.full-width-mobile { width: 100% !important; }\n" +
                    "      \t\t\t\ttd.full-width-mobile { width: auto !important; }\n" +
                    "      \n" +
                    "      }\n" +
                    "      @media only screen and (min-width:480px) {\n" +
                    "      .dys-column-per-100 {\n" +
                    "      \twidth: 100.000000% !important;\n" +
                    "      \tmax-width: 100.000000%;\n" +
                    "      }\n" +
                    "      }\n" +
                    "      @media only screen and (min-width:480px) {\n" +
                    "      .dys-column-per-50 {\n" +
                    "      \twidth: 50.000000% !important;\n" +
                    "      \tmax-width: 50.000000%;\n" +
                    "      }\n" +
                    "      .dys-column-per-100 {\n" +
                    "      \twidth: 100.000000% !important;\n" +
                    "      \tmax-width: 100.000000%;\n" +
                    "      }\n" +
                    "      .dys-column-per-90 {\n" +
                    "      \twidth: 90% !important;\n" +
                    "      \tmax-width: 90%;\n" +
                    "      }\n" +
                    "      }\n" +
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div>\n" +
                    "      <div style='background:#4DBFBF;background-color:#4DBFBF;margin:0px auto;max-width:600px;'>\n" +
                    "        <table align='center' border='0' cellpadding='0' cellspacing='0' role='presentation' style='background:#4DBFBF;background-color:#4DBFBF;width:100%;'>\n" +
                    "          <tbody>\n" +
                    "            <tr>\n" +
                    "              <td style='direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;'>\n" +
                    "      <div class='dys-column-per-100 outlook-group-fix' style='direction:ltr;display:inline-block;font-size:13px;text-align:left;vertical-align:top;width:100%;'>\n" +
                    "                  <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='vertical-align:top;' width='100%'>\n" +
                    "                    <tr>\n" +
                    "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\n" +
                    "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:collapse;border-spacing:0px;'>\n" +
                    "                          <tbody>\n" +
                    "                            <tr>\n" +
                    "                              <td style='width:216px;'>\n" +
                    "                                <img alt='Descriptive Alt Text' height='189' src='https://assets.opensourceemails.com/imgs/neopolitan/robot-happy.png' style='border:none;display:block;font-size:13px;height:189px;outline:none;text-decoration:none;width:100%;' width='216' />\n" +
                    "                              </td>\n" +
                    "                            </tr>\n" +
                    "                          </tbody>\n" +
                    "                        </table>\n" +
                    "                      </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;'>\n" +
                    "                        <div style=\"color:#FFFFFF;font-family:'Droid Sans', 'Helvetica Neue', Arial, sans-serif;font-size:36px;line-height:1;text-align:center;\">\n" +
                    "                          Welcome!\n" +
                    "                        </div>\n" +
                    "                      </td>\n" +
                    "                    </tr>\n" +
                    "                    <tr>\n" +
                    "                      <td align='center' style='font-size:0px;padding:10px 25px;word-break:break-word;' vertical-align='middle'>\n" +
                    "                        <table border='0' cellpadding='0' cellspacing='0' role='presentation' style='border-collapse:separate;line-height:100%;width:200px;'>\n" +
                    "                          <tr>\n" +
                    "                            <td align='center' bgcolor='#178F8F' role='presentation' style='background-color:#178F8F;border:none;border-radius:4px;cursor:auto;padding:10px 25px;' valign='middle'>\n" +
                    "                              <a href='http://localhost:8081/api/patients/confirm-registration/"
                    + myToken + "' style=\"background:#178F8F;color:#ffffff;font-family:'Droid Sans', 'Helvetica Neue', Arial, sans-serif;font-size:16px;font-weight:bold;line-height:30px;margin:0;text-decoration:none;text-transform:none;\" target='_blank'>\n" +
                    "                                Activate account!\n" +
                    "                              </a>\n" +
                    "                            </td>\n" +
                    "                          </tr>\n" +
                    "                        </table>\n" +
                    "                      </td>\n" +
                    "                    </tr>\n" +
                    "                  </table>\n" +
                    "                </div>\n" +
                    "              </td>\n" +
                    "            </tr>\n" +
                    "          </tbody>\n" +
                    "        </table>\n" +
                    "      </div>\n" +
                    "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px;\" width=\"600\"><tr><td \n" +
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>";
            confirmationTokenRepository.save(confirmationToken);
            message.setContent(poruka, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return savedPatient;
    }
}
