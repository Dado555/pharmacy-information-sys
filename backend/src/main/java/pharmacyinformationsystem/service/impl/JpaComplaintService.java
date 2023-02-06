package pharmacyinformationsystem.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.ConfirmationToken;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.repository.ComplaintRepository;
import pharmacyinformationsystem.repository.ConfirmationTokenRepository;
import pharmacyinformationsystem.service.ComplaintService;
import pharmacyinformationsystem.service.SystemAdminService;
import pharmacyinformationsystem.service.base.ComplaintServiceBase;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Service
@Transactional
public class JpaComplaintService extends ComplaintServiceBase implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final SystemAdminService systemAdminService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public JpaComplaintService(ComplaintRepository complaintRepository, SystemAdminService systemAdminService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.complaintRepository = complaintRepository;
        this.systemAdminService = systemAdminService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Complaint> findAll() {
        return complaintRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Complaint findOne(Integer id) {
        return complaintRepository.findById(id).orElse(null);
    }

    @Override
    public Complaint save(Complaint entity) {
        return complaintRepository.save(entity);
    }

    @Override
    public List<Complaint> getComplaintsForSystemAdmin(Integer id) {
        SystemAdmin systemAdmin = systemAdminService.findOne(id);
        return new ArrayList<>(systemAdmin.getComplaints());
    }

    @Override
    public List<Complaint> getComplaintsWithoutSystemAdmin() {
        return complaintRepository.findAllBySystemAdminIsNull();
    }

    @Override
    public Complaint updateComplaint(Complaint complaint, Integer id) {
        Complaint c = findOne(complaint.getId());
        if (c == null || c.getSystemAdmin() != null)
            return null;
        SystemAdmin systemAdmin = systemAdminService.findOne(id);

        if (systemAdmin == null)
            return null;

        c.update(complaint.getComplaintResponse());
        c.setSystemAdmin(systemAdmin);
        sendConfirmationMail(c);
        return save(c);
    }

    @Override
    public Boolean sendConfirmationMail(Complaint complaint) {
        String myToken = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(complaint.getId(), myToken);
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        String recipient = complaint.getPatient().getEmail();
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
            message.setSubject("Complaint answer");
            String content = MailConstants.getMailMessage("Complaint answer", "Your complaint: " +
                    complaint.getComplaintMessage() + " <br>Response: " + complaint.getComplaintResponse());
            confirmationTokenRepository.save(confirmationToken);
            message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
