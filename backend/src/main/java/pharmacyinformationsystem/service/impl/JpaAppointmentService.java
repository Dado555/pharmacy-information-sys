package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Penalty;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.AppointmentRepository;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.service.base.AppointmentServiceBase;

import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@Transactional
public class JpaAppointmentService extends AppointmentServiceBase implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final PatientService patientService;
    private final PharmacyService pharmacyService;
    private final PharmacistService pharmacistService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JpaAppointmentService(AppointmentRepository appointmentRepository, PatientService patientService, PharmacyService pharmacyService, PharmacistService pharmacistService) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.pharmacyService = pharmacyService;
        this.pharmacistService = pharmacistService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment findOne(Integer id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findScheduledAppointmentsForPatient(Integer patientId) {
        if (patientService.findOne(patientId) == null)
            throw new NotFoundException("Patient not found!");

        return appointmentRepository.findScheduledAppointmentsForPatient(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findAppointmentHistoryForPatient(Integer patientId) {
        if (patientService.findOne(patientId) == null)
            throw new NotFoundException("Patient not found!");

        return appointmentRepository.findAppointmentHistoryForPatient(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findAllBySearchCriteria(Integer patientId, String sortBy) {
        if (patientService.findOne(patientId) == null)
            throw new NotFoundException("Patient not found!");

        switch (sortBy) {
            case "date-ascending":
                return appointmentRepository.findAllByPatientAndOrderByStartDateAndTimeAsc(patientId);
            case "date-descending":
                return appointmentRepository.findAllByPatientAndOrderByStartDateAndTimeDesc(patientId);
            case "price-ascending":
                return appointmentRepository.findAllByPatientAndOrderByPriceAsc(patientId);
            case "price-descending":
                return appointmentRepository.findAllByPatientAndOrderByPriceDesc(patientId);
            default:
                return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findFreeAppointments(Integer pharmacyId) {
        return appointmentRepository.findFreeAppointments(pharmacyId);
    }

    @Override
    public Appointment updateAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = findOne(appointmentDTO.getId());
        Patient patient = patientService.findOne(appointmentDTO.getPatientId());
        if (appointment == null || patient == null)
            throw new NotFoundException("Patient or appointment not found!");

        appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
        appointment.setPatient(patient);
        save(appointment);
        return appointment;
    }

    @Override
    public Appointment scheduleAppointmentCounseling(Integer patientId, AppointmentDTO appointmentDTO) {
        if ((appointmentDTO.getStart() - System.currentTimeMillis()) < 86400000)
            throw new BadRequestException("Invalid date!");

        Patient patient;
        try { patient = patientService.findOneWithLock(patientId); }
        catch (Exception e) { throw new BadRequestException("Someone already scheduled an appointment in the same time as you. Please wait and then try again."); }

        if (patient == null)
            throw new NotFoundException("Patient not found!");

        if (patient.getPenalties().size() >= 3)
            throw new BadRequestException("Patient with 3 or more penalties is not allowed to schedule an appointment!");

        Pharmacy pharmacy = pharmacyService.findOne(appointmentDTO.getPharmacyId());
        if (pharmacy == null)
            throw new NotFoundException("Pharmacy not found!");

        Pharmacist pharmacist;
        try { pharmacist = pharmacistService.findOneWithLock(appointmentDTO.getWorkerId()); }
        catch (Exception e) { throw new BadRequestException("Someone already scheduled an appointment in the same time as you. Please wait and then try again."); }

        if (pharmacist == null)
            throw new NotFoundException("Pharmacist not found!");

        if (pharmacyService.findPharmacistsByDateAndTimeCriteria(appointmentDTO.getPharmacyId(), appointmentDTO.getStart(), appointmentDTO.getEnd() - appointmentDTO.getStart()).stream().noneMatch(p -> pharmacist.getId().equals(p.getId())))
            throw new BadRequestException("Appointment is already reserved.");

        Appointment appointment = new Appointment(appointmentDTO.getStart(), appointmentDTO.getEnd(), pharmacy.getCounselingPrice(), AppointmentStatus.RESERVED, AppointmentType.COUNSELING, patient, pharmacist, pharmacy);
        patient.setPoints(patient.getPoints() + appointment.getPharmacy().getPoint().getPharmacistCounseling());
        save(appointment);
        patientService.save(patient);
        return appointment;
    }

    @Override
    public Appointment scheduleAppointmentExamination(Integer patientId, Integer appointmentId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        if (patient.getPenalties().size() >= 3)
            throw new BadRequestException("Patient with 3 or more penalties is not allowed to schedule an appointment!");

        Appointment appointment = findOne(appointmentId);
        if (appointment == null)
            throw new NotFoundException("Appointment not found!");

        if (appointment.getAppointmentStatus().equals(AppointmentStatus.RESERVED))
            throw new BadRequestException("Appointment already reserved");

        try {
            appointment.setPatient(patient);
            appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
            save(appointment);
            return appointment;
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new BadRequestException("Someone already scheduled this examination. Try again.");
        }
    }

    @Override
    public Appointment cancelAppointmentCounseling(Integer patientId, Integer appointmentId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Appointment appointment = findOne(appointmentId);
        if (appointment == null)
            throw new NotFoundException("Appointment not found!");
        if ((appointment.getStartDateAndTime() - System.currentTimeMillis()) < 86_400_000)
            throw new BadRequestException("Appointment can only be canceled 24 hours before the scheduling date!");

        appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
        save(appointment);
        return appointment;
    }

    @Override
    public Appointment cancelAppointmentExamination(Integer patientId, Integer appointmentId) {
        Patient patient = patientService.findOne(patientId);
        if (patient == null)
            throw new NotFoundException("Patient not found!");

        Appointment appointment = findOne(appointmentId);
        if (appointment == null)
            throw new NotFoundException("Appointment not found!");
        if ((appointment.getStartDateAndTime() - System.currentTimeMillis()) < 86_400_000)
            throw new BadRequestException("Appointment can only be canceled 24 hours before the scheduling date!");

        appointment.setAppointmentStatus(AppointmentStatus.FREE);
        appointment.setPatient(null);
        save(appointment);
        return appointment;
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
            message.setSubject("Appointment confirmation");
            String content = MailConstants.getMailMessage("Appointment ID: " + id, "Your scheduled appointment is confirmed!");
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getFreeAppointments(Integer dermatologistId, Integer pharmacyId) {
        return appointmentRepository.findFreeAppointments(dermatologistId, pharmacyId, System.currentTimeMillis() + 86400000);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getDoneAppointments(Integer medicalWorkerId) {
        return appointmentRepository.findDoneAppointments(medicalWorkerId);
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void setPenaltiesForPastAppointments() {
        for(Appointment a : findAll()) {
            if (a.getAppointmentStatus() == AppointmentStatus.RESERVED && a.getStartDateAndTime() < System.currentTimeMillis()) {
                a.setAppointmentStatus(AppointmentStatus.CANCELED);
                save(a);

                Patient patient = a.getPatient();
                Penalty penalty = new Penalty(System.currentTimeMillis(), "Patient did not appear for the appointment.", patient);
                patient.addPenalty(penalty);
                patient.getPenalties().add(penalty);
                patientService.save(patient);
            }
        }
    }

    @Override
    public Appointment save(Appointment entity) {
        return this.appointmentRepository.save(entity);
    }

    @Override
    public Appointment saveWithLock(Appointment appointment) {
        Appointment app = entityManager.find(Appointment.class, appointment.getId()); //orderRepository.getOne(orderId);
        entityManager.lock(app, LockModeType.PESSIMISTIC_WRITE);
        return this.appointmentRepository.save(appointment);
    }
}
