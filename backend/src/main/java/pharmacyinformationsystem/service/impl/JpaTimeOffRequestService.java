package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.TimeOffRequest;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.PharmacyAdminRepository;
import pharmacyinformationsystem.repository.TimeOffRequestRepository;
import pharmacyinformationsystem.service.SystemAdminService;
import pharmacyinformationsystem.service.TimeOffRequestService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

@Service
@Transactional
public class JpaTimeOffRequestService implements TimeOffRequestService {
    private final TimeOffRequestRepository timeOffRequestRepository;
    private final SystemAdminService systemAdminService;
    private final PharmacyAdminRepository pharmacyAdminRepository;
    private final UserService userService;

    @Autowired
    public JpaTimeOffRequestService(TimeOffRequestRepository timeOffRequestRepository, SystemAdminService systemAdminService,
                                    PharmacyAdminRepository pharmacyAdminRepository, UserService userService) {
        this.timeOffRequestRepository = timeOffRequestRepository;
        this.systemAdminService = systemAdminService;
        this.pharmacyAdminRepository = pharmacyAdminRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeOffRequest> findAll() {
        return timeOffRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TimeOffRequest findOne(Integer id) {
        return timeOffRequestRepository.findById(id).orElse(null);
    }

    @Override
    public TimeOffRequest save(TimeOffRequest entity) {
        return timeOffRequestRepository.save(entity);
    }

    @Override
    public TimeOffRequest createTimeOffRequest(TimeOffRequest request, Integer medicalWorkerId, String role) {
        MedicalWorker mw = (MedicalWorker) userService.findOne(medicalWorkerId);
        request.setMedicalWorker(mw);
        String recipient = "";
        Date date1 = new Date(request.getStartDate());
        Date date2 = new Date(request.getEndDate());
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy.");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startDate = formatter.format(date1);
        String endDate = formatter.format(date2);

        if (role.equals("PHARMACIST")) {
            recipient = pharmacyAdminRepository.findOneByPharmacyId(((Pharmacist) mw).getPharmacy().getId()).getEmail();
            request.setPharmacy(((Pharmacist)mw).getPharmacy());
        } else {
            recipient = systemAdminService.findAll().get(0).getEmail();
        }

        sendTimeOffRequestEmail(recipient, startDate, endDate, mw, request.getContent());

        return save(request);
    }

    @Override
    public void approveTimeOff(Integer requestId, Boolean status) {
        TimeOffRequest timeOffRequest = timeOffRequestRepository.getOne(requestId);
        timeOffRequest.setApproved(status);
        timeOffRequestRepository.save(timeOffRequest);
        // approveTimeOffRequestEmail(timeOffRequest, status);
    }

    @Override
    @Async
    @Transactional(propagation= Propagation.NEVER)
    public void approveTimeOffRequestEmail(TimeOffRequest timeOffRequest, Boolean status)
    {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(timeOffRequest.getMedicalWorker().getEmail()));
            String subject = "Your time off request has been ";
            String content = "Your time off request from " +
                    Instant.ofEpochMilli(timeOffRequest.getStartDate()).atZone(ZoneId.systemDefault()).toLocalDate() +
                    " to " + Instant.ofEpochMilli(timeOffRequest.getEndDate()).atZone(ZoneId.systemDefault()).toLocalDate()+
                    " has been ";
            if(status) {
                subject += " approved.";
                content += " approved!";
            } else {
                subject += " rejected.";
                content += " rejected!";
            }
            message.setSubject(subject);
            String nesto = MailConstants.getMailMessage(subject, content);
            message.setContent(nesto, "text/html");
            //message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional(propagation= Propagation.NEVER)
    public void sendTimeOffRequestEmail(String recipient, String startDate, String endDate, MedicalWorker mw, String content) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        String email = "isa.confirmtoken@gmail.com";
        String password = "sifra1234";
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
            message.setSubject("Time Off Request");
            String msg =
                    content + "<br><br>" +
                            "Start date: " + startDate +  "<br>" +
                            "End date: " + endDate + "<br>" +
                            "Medical worker: " + mw.getFirstName() + " " + mw.getLastName() + "<br>" +
                            "Email: " + mw.getEmail();

            message.setContent(msg, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

