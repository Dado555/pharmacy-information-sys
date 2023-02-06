package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.EPrescriptionStatus;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.repository.ConfirmationTokenRepository;
import pharmacyinformationsystem.repository.QRCodeResultRepository;
import pharmacyinformationsystem.service.EPrescriptionService;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.service.PharmacyMedicineService;
import pharmacyinformationsystem.service.QRCodeResultService;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.LockModeType;
import java.util.*;

@Service
@Transactional
public class JpaQRCodeResultService implements QRCodeResultService {

    private final QRCodeResultRepository qrCodeResultRepository;
    private final EPrescriptionService ePrescriptionService;
    private final PharmacyMedicineService pharmacyMedicineService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PatientService patientService;

    @Autowired
    public JpaQRCodeResultService(QRCodeResultRepository qrCodeResultRepository, EPrescriptionService ePrescriptionService, PharmacyMedicineService pharmacyMedicineService, ConfirmationTokenRepository confirmationTokenRepository, PatientService patientService) {
        this.qrCodeResultRepository = qrCodeResultRepository;
        this.ePrescriptionService = ePrescriptionService;
        this.pharmacyMedicineService = pharmacyMedicineService;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.patientService = patientService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QRCodeResult> findAll() {
        return qrCodeResultRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public QRCodeResult findOne(Integer id) {
        return qrCodeResultRepository.findById(id).orElse(null);
    }

    @Override
    public QRCodeResult save(QRCodeResult entity) {
        return qrCodeResultRepository.save(entity);
    }

    @Override
    public List<QRCodeResult> getMedicinesFromQR(String data, Integer id) {
        String[] getEPresId = data.split(":");
        String ePresId = getEPresId[1].substring(1, getEPresId[1].length() - 2);
        EPrescription ePrescription = ePrescriptionService.findOne(Integer.parseInt(ePresId));

        List<QRCodeResult> qrCodeResults = new ArrayList<>();

        if (ePrescription.getStatus().equals(EPrescriptionStatus.NEW)) {
            List<Pharmacy> pharmacies = new ArrayList<>();
            HashMap<PharmacyMedicine, Integer> pricesInPharmacies = new HashMap<>();

            for (EPrescriptionMedicine ePrescriptionMedicine : ePrescription.getEPrescriptionMedicineList()) {
                List<PharmacyMedicine> pharmacyMedicineList = pharmacyMedicineService.findPharmaciesForMedicineAndAmount(ePrescriptionMedicine.getMedicine().getId(), ePrescriptionMedicine.getAmount());
                for (PharmacyMedicine pharmacyMedicine : pharmacyMedicineList) {
                    pricesInPharmacies.put(pharmacyMedicine, pharmacyMedicine.getPharmacy().getId());
                    if (!pharmacies.contains(pharmacyMedicine.getPharmacy())) {
                        pharmacies.add(pharmacyMedicine.getPharmacy());
                    }
                }
            }
            for (Pharmacy pharmacy : pharmacies) {
                double totalPrice = 0.0;
                int counter = 0;
                for (EPrescriptionMedicine ePrescriptionMedicine : ePrescription.getEPrescriptionMedicineList()) {
                    for (HashMap.Entry<PharmacyMedicine, Integer> entry : pricesInPharmacies.entrySet()) {
                        if (entry.getValue().equals(pharmacy.getId()) && entry.getKey().getMedicine().equals(ePrescriptionMedicine.getMedicine())) {
                            totalPrice += ePrescriptionMedicine.getAmount() * entry.getKey().getPrice();
                            counter++;
                        }
                    }
                }
                if (counter == ePrescription.getEPrescriptionMedicineList().size()) {
                    QRCodeResult qrCodeResult = new QRCodeResult(pharmacy, totalPrice, ePrescription);
                    qrCodeResults.add(qrCodeResult);
                    save(qrCodeResult);
                }
            }
        }
        return qrCodeResults;
    }

    @Override
    public Boolean buyMedicinesAfterQR(Integer patientId, Integer qrCodeResultId) {
        QRCodeResult qrCodeResult = findOne(qrCodeResultId);
        qrCodeResult.getEPrescription().setStatus(EPrescriptionStatus.PROCESSED);
        qrCodeResult.setActive(false);
        qrCodeResultRepository.save(qrCodeResult);
        for (EPrescriptionMedicine ePrescriptionMedicine : qrCodeResult.getEPrescription().getEPrescriptionMedicineList()) {
            PharmacyMedicine pharmacyMedicine = pharmacyMedicineService.findPharmacyMedicine(qrCodeResult.getPharmacy().getId(),
                    ePrescriptionMedicine.getMedicine().getId());
            Integer oldAmount = pharmacyMedicine.getAvailableAmount();
            pharmacyMedicine.setAvailableAmount(oldAmount - ePrescriptionMedicine.getAmount());
            pharmacyMedicineService.save(pharmacyMedicine);
        }
        sendConfirmationMail(patientService.findOne(patientId), qrCodeResult);
        return true;
    }

    @Override
    public Boolean sendConfirmationMail(Patient patient, QRCodeResult qrCodeResult) {
        String myToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(qrCodeResult.getId(), myToken);
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
            message.setSubject("Medicine purchase confirmation");
            Date d = new Date(qrCodeResult.getEPrescription().getDateOfIssue());
            StringBuilder content = new StringBuilder("Your medicine purchase is confirmed!<br>Pharmacy: " + qrCodeResult.getPharmacy().getName() +
                    "<br>Total price: " + qrCodeResult.getTotalPrice() + "<br>Date of issue: " + d);
            for (EPrescriptionMedicine ePrescriptionMedicine : qrCodeResult.getEPrescription().getEPrescriptionMedicineList()) {
                content.append("<br>Medicine: ").append(ePrescriptionMedicine.getMedicine().getName());
                content.append(" Amount: ").append(ePrescriptionMedicine.getAmount());
                content.append(" Therapy duration: ").append(ePrescriptionMedicine.getTherapyDuration()).append(" days<br>");
            }
            String allContent = MailConstants.getMailMessage("Medicine purchase: " + qrCodeResult.getId(), content.toString());
            confirmationTokenRepository.save(confirmationToken);
            message.setContent(allContent, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
