package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.MedicineReservationItem;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.MedicineReservationItemRepository;
import pharmacyinformationsystem.repository.MedicineReservationRepository;
import pharmacyinformationsystem.service.MedicineReservationService;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.service.PharmacistService;
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
public class JpaMedicineReservationService implements MedicineReservationService {

    private final MedicineReservationRepository repository;
    private final MedicineReservationItemRepository itemRepository;

    private final PharmacistService pharmacistService;

    @Autowired
    public JpaMedicineReservationService(MedicineReservationRepository repository, MedicineReservationItemRepository itemRepository, PharmacistService pharmacistService) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.pharmacistService = pharmacistService;
    }

    @Override
    public List<MedicineReservation> findAll() {
        return repository.findAll();
    }

    @Override
    public MedicineReservation findOne(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MedicineReservation save(MedicineReservation entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null)
            return;

        repository.deleteById(id);
    }

    @Override
    public List<MedicineReservationItem> getReservationItems(Integer reservationId, Integer pharmacistId) {
        Pharmacist pharmacist = pharmacistService.findOne(pharmacistId);
        return itemRepository.findReservationItemsForPharmacy(reservationId, pharmacist.getPharmacy().getId(), (new Date()).getTime());
    }

    @Override
    public MedicineReservationItem issueReservationItem(Integer reservationItemId) {
        MedicineReservationItem mri = itemRepository.findOneById(reservationItemId);

        try {
        mri.setIssued(true);
        itemRepository.save(mri);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Medicine already issued.");
        }

        Patient patient = mri.getMedicineReservation().getPatient();
        if (patient.getPoints() == null) {
            patient.setPoints(0.0);
        }
        Double points = patient.getPoints() + mri.getPharmacyMedicine().getMedicine().getPoints();
        patient.setPoints(points);

        return mri;
    }
    
    @Async
    @Transactional(propagation=Propagation.NEVER)
    @Override
    public void sendIssueConfirmationEmail(MedicineReservationItem mri, Double points) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        String recipient = mri.getMedicineReservation().getPatient().getEmail();
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
            message.setSubject(mri.getPharmacyMedicine().getMedicine().getName() + " issued successfully!");
            String msg =
                    "Medicine: " + mri.getPharmacyMedicine().getMedicine().getName() + "<br>" +
                            "Amount: " + mri.getAmount() + "<br>" +
                            "Pharmacy: " + mri.getPharmacyMedicine().getPharmacy().getName() + "<br>" +
                            "Discount points earned: " + points;

            message.setContent(msg, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MedicineReservationItem> getMedicineReservationItems(Integer reservationId) {
        MedicineReservation reservation = findOne(reservationId);
        return new ArrayList<>(reservation.getReservationItems());
    }

}
