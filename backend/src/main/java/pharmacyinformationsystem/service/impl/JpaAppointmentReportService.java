package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.repository.*;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentReportDTO;
import pharmacyinformationsystem.web.dto.domain.PrescriptionMedicineDTO;
import pharmacyinformationsystem.web.exception.BadRequestException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
@Transactional
public class JpaAppointmentReportService implements AppointmentReportService {
    private final AppointmentReportRepository appointmentReportRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final TimeOffRequestRepository timeOffRequestRepository;
    private final PrescriptionRepository prescriptionRepository;

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final UserService userService;
    private final PharmacyService pharmacyService;
    private final MedicineService medicineService;

    @Autowired
    public JpaAppointmentReportService(AppointmentReportRepository appointmentReportRepository, WorkScheduleRepository workScheduleRepository, AppointmentRepository appointmentRepository, TimeOffRequestRepository timeOffRequestRepository, PrescriptionRepository prescriptionRepository, AppointmentService appointmentService, PatientService patientService, UserService userService, PharmacyService pharmacyService, MedicineService medicineService) {
        this.appointmentReportRepository = appointmentReportRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.timeOffRequestRepository = timeOffRequestRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.userService = userService;
        this.pharmacyService = pharmacyService;
        this.medicineService = medicineService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentReport> findAll() {
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentReport findOne(Integer id) {
        return null;
    }

    @Override
    public AppointmentReport save(AppointmentReport entity) {
        return appointmentReportRepository.save(entity);
    }

    @Override
    public AppointmentReport createAppointmentReport(AppointmentReportDTO appointmentReportDTO) {
        AppointmentReport ar = new AppointmentReport();
        Patient p = patientService.findOne(appointmentReportDTO.getPatientId());
        Prescription ep = new Prescription();
        List<PrescriptionMedicine> medicines = new ArrayList<>();
        ar.setAppointmentInfo(appointmentReportDTO.getAppointmentInfo());
        ar.setPrescription(ep);
        ep.setDateOfIssue(System.currentTimeMillis());
        ep.setPrescriptionMedicineList(medicines);

        prescriptionRepository.save(ep);

        for (PrescriptionMedicineDTO epmDTO : appointmentReportDTO.getMedicines()) {
            PrescriptionMedicine epm = new PrescriptionMedicine();
            Medicine m = medicineService.findOne(epmDTO.getId());
            epm.setPrescription(ep);
            epm.setAmount(epmDTO.getAmount());
            epm.setMedicine(m);
            epm.setTherapyDuration(epmDTO.getTherapy());

            medicines.add(epm);
        }

        Appointment a = appointmentService.findOne(appointmentReportDTO.getAppointmentId());
        a.setAppointmentStatus(AppointmentStatus.DONE);
        if (a.getAppointmentType() == AppointmentType.COUNSELING)
            patientService.addPointsToPatient(p.getId(), a.getPharmacy().getPoint().getPharmacistCounseling());
        if (a.getAppointmentType() == AppointmentType.EXAMINATION)
            patientService.addPointsToPatient(p.getId(), a.getPharmacy().getPoint().getDermatologistExamination());

        patientService.save(p);
        return save(ar);
    }

    @Override
    public Appointment createNewAppointment(AppointmentDTO appointmentDTO) {
        Patient p = patientService.findOne(appointmentDTO.getPatientId());
        MedicalWorker mw = (MedicalWorker) userService.findOne(appointmentDTO.getWorkerId());
        Pharmacy pharmacy = pharmacyService.findOne(appointmentDTO.getPharmacyId());

        if ((appointmentRepository.findOverlappingForPatient(appointmentDTO.getStart(), appointmentDTO.getEnd(), p.getId())).size() > 0) {
            throw new BadRequestException("Invalid date and time!");
        }

        if ((appointmentRepository.findOverlappingForMedicalWorker(appointmentDTO.getStart(), appointmentDTO.getEnd(), mw.getId())).size() > 0) {
            throw new BadRequestException("Invalid date and time!");
        }

        if ((timeOffRequestRepository.findOverlappingForMedicalWorker(appointmentDTO.getStart(), appointmentDTO.getEnd(), mw.getId())).size() > 0) {
            throw new BadRequestException("Invalid date and time!");
        }

        Appointment a = new Appointment();

        a.setStartDateAndTime(appointmentDTO.getStart());
        a.setEndDateAndTime(appointmentDTO.getEnd());
        a.setMedicalWorker(mw);
        a.setPharmacy(pharmacy);
        a.setAppointmentType(appointmentDTO.getTitle());
        a.setPatient(p);
        a.setAppointmentStatus(AppointmentStatus.RESERVED);
        a.setPrice(1000.0);

        validateWorkSchedule(a);

        sendInfoEmail(a);

        return appointmentService.save(a);
    }

    public void validateWorkSchedule(Appointment a) {
        WorkSchedule ws = workScheduleRepository.findByPharmacyIdAndMedicalWorkerId(a.getPharmacy().getId(), a.getMedicalWorker().getId());
        int minutesStart = (int) ((a.getStartDateAndTime()  / (1000 * 60)) % 60);
        int hoursStart = (int) ((a.getStartDateAndTime()  / (1000 * 60 * 60)) % 24);
        int minutesEnd = (int) ((a.getEndDateAndTime()  / (1000 * 60)) % 60);
        int hoursEnd = (int) ((a.getEndDateAndTime()  / (1000 * 60 * 60)) % 24);
        int startTime = minutesStart + hoursStart * 60 + 2 * 60;
        int endTime = minutesEnd + hoursEnd * 60 + 2 * 60;

        if (startTime < ws.getStartTime() || startTime > ws.getEndTime()) {
            throw new BadRequestException("Invalid date and time!");
        }

        if (endTime < ws.getStartTime() || endTime > ws.getEndTime()) {
            throw new BadRequestException("Invalid date and time!");
        }
    }

    @Override
    public Appointment reserveAppointment(Integer id, Integer patientId) {
        Appointment a = appointmentService.findOne(id);
        Patient p = patientService.findOne(patientId);

        if ((appointmentRepository.findOverlappingForPatient(a.getStartDateAndTime(), a.getEndDateAndTime(), patientId)).size() > 0) {
                throw new BadRequestException("Invalid date and time!");
        }

        if ((timeOffRequestRepository.findOverlappingForMedicalWorker(a.getStartDateAndTime(), a.getEndDateAndTime(), a.getMedicalWorker().getId())).size() > 0) {
            throw new BadRequestException("Invalid date and time!");
        }

        a.setAppointmentStatus(AppointmentStatus.RESERVED);
        a.setPatient(p);

        sendInfoEmail(a);

        return appointmentService.save(a);
    }

    @Async
    @Transactional(propagation= Propagation.NEVER)
    public void sendInfoEmail(Appointment a) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        String recipient = a.getPatient().getEmail();
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
            message.setSubject("New " + a.getAppointmentType() + " scheduled!");
            String msg =
                    "Start date and time: " + a.getStartDateAndTime() + "<br>" +
                            "Pharmacy: " + a.getPharmacy() + "<br>" +
                            "Medical worker: " + a.getMedicalWorker().getFirstName() + " " + a.getMedicalWorker().getLastName() + "<br>" +
                            "Email: " + a.getMedicalWorker().getEmail();

            message.setContent(msg, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
