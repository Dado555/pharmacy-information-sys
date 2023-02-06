package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.repository.*;
import pharmacyinformationsystem.service.MedicineInquiryService;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.service.PharmacyMedicineService;
import pharmacyinformationsystem.web.dto.PharmacyMedicineEdit;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;
import pharmacyinformationsystem.web.util.MailConstants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class JpaPharmacyMedicineService implements PharmacyMedicineService {

    private final PharmacyMedicineRepository pharmacyMedicineRepository;
    private final PharmacyAdminRepository pharmacyAdminRepository;

    private final PatientService patientService;
    private final MedicineInquiryService medicineInquiryService;
    private final PharmedActionPriceRepository actionPriceRepository;

    @Autowired
    public JpaPharmacyMedicineService(PharmacyMedicineRepository pharmacyMedicineRepository,
                                      PharmacyAdminRepository pharmacyAdminRepository, PatientService patientService,
                                      MedicineInquiryService medicineInquiryService, PharmedActionPriceRepository actionPriceRepository) {
        this.pharmacyMedicineRepository = pharmacyMedicineRepository;
        this.pharmacyAdminRepository = pharmacyAdminRepository;
        this.patientService = patientService;
        this.medicineInquiryService = medicineInquiryService;
        this.actionPriceRepository = actionPriceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicine> findAll() {
        return pharmacyMedicineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicine> findPharmaciesForMedicine(Integer id) {
        return pharmacyMedicineRepository.findPharmacyMedicineByMedicine(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicine> findPharmaciesForMedicineAndAmount(Integer medicineId, Integer amount) {
        return pharmacyMedicineRepository.findPharmacyMedicinesByMedicineAndAvailableAmount(medicineId, amount);
    }

    @Override
    public Integer prescribeMedicine(Map<String, Integer> patientAmount, Integer id) {
        PharmacyMedicine pharmacyMedicine = findOne(id);
        Patient patient = patientService.findOne(patientAmount.get("patientId"));
        Integer amount = patientAmount.get("amount");
        if (pharmacyMedicine == null || patient == null)
            throw new NotFoundException("PharmacyMedicine or Patient not found!");

        if (patient.getAllergicMedicines().contains(pharmacyMedicine.getMedicine())) {
            return -2;
        }

        if (pharmacyMedicine.getAvailableAmount() < amount) {
            // slanje mejla adminu apoteke
            MedicineInquiry mi = new MedicineInquiry();
            mi.setMedicine(pharmacyMedicine.getMedicine());
            mi.setPharmacy(pharmacyMedicine.getPharmacy());
            mi.setDateCreated(System.currentTimeMillis());
            mi.setActive(true);
            mi.setResolved(false);
            medicineInquiryService.save(mi);
            String recipient = pharmacyAdminRepository.findOneByPharmacyId(pharmacyMedicine.getPharmacy().getId()).getEmail();
            sendMedicineOutOfStockMail(mi, recipient);
            return -1;
        }

        try {
        updateMedicine(id, pharmacyMedicine.getPrice(), pharmacyMedicine.getAvailableAmount() - amount);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Resource currently unavailable. Try again in a minute.");
        }
        return 1;
    }

    @Override
    public Integer removeMedicine(Map<String, Integer> patientAmount, Integer id) {
        PharmacyMedicine pharmacyMedicine = findOne(id);
        Integer amount = patientAmount.get("amount");


        try {
        updateMedicine(id, pharmacyMedicine.getPrice(), pharmacyMedicine.getAvailableAmount() + amount);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Resource currently unavailable. Try again in a minute.");
        }
        return 1;
    }

    @Override
    public List<Integer> medicinesSoldMonthly(Integer id) {
        PharmacyMedicine medicine = findOne(id);
        return soldMonthly(medicine.getReservationItems());
    }

    @Override
    public List<Integer> medicinesSoldYearly(Integer id) {
        PharmacyMedicine medicine = findOne(id);
        return soldYearly(medicine.getReservationItems());
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyMedicine findOne(Integer id) {
        return pharmacyMedicineRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PharmacyMedicine> searchByCriteria(MedicineSearchParameters searchParameters, Optional<Integer> page) {
        Pageable pageable = PageRequest.of(0, 18);
        if (!searchParameters.getSortBy().equals("rating") && !searchParameters.getSortBy().equals("") && searchParameters.getSortBy() != null) {
            Sort sort = Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(0, 18, sort);
        }
        return pharmacyMedicineRepository.findByNameAndTypeAndPharmacyIdAndFilterByAverageRating(searchParameters.getName(),
                searchParameters.getType(), searchParameters.getPharmacyId(), Double.valueOf(searchParameters.getRatingFrom()),
                Double.valueOf(searchParameters.getRatingTo()), pageable);
    }

    @Override
    public void deleteByMedicineIdAndPharmacyId(Integer medicineId, Integer pharmacyId) {
        pharmacyMedicineRepository.deletePharmacyMedicineByIdAndPharmacyId(medicineId, pharmacyId);
    }

    @Override
    public void updateMedicine(Integer id, Double price, Integer availableAmount) {
        pharmacyMedicineRepository.updatePharmacyMedicine(id, price, availableAmount);
    }

    @Override
    public PharmacyMedicine findPharmacyMedicine(Integer pharmacyId, Integer medicineId) {
        return pharmacyMedicineRepository.findPharmacyMedicineByMedicineIdAndPharmacyId(medicineId, pharmacyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicine> findReplacementMedicines(Integer id) {
        PharmacyMedicine pharmacyMedicine = pharmacyMedicineRepository.findById(id).orElse(null);
        if (pharmacyMedicine == null)
            throw  new NotFoundException("PharmacyMedicine not found!");

        Integer pharmacyId = pharmacyMedicine.getPharmacy().getId();
        List<PharmacyMedicine> pharmacyReplacementMedicines = new ArrayList<>();
        for(Medicine m : pharmacyMedicine.getMedicine().getReplacementMedicines()) {
            PharmacyMedicine pharMed = pharmacyMedicineRepository.findPharmacyMedicineByMedicineIdAndPharmacyId(m.getId(), pharmacyId);
            if (pharMed != null) {
                pharmacyReplacementMedicines.add(pharMed);
            }
        }

        return pharmacyReplacementMedicines;
    }

    @Override
    public PharmacyMedicine save(PharmacyMedicine entity) {
        if (entity == null)
            return null;
        return pharmacyMedicineRepository.save(entity);
    }

    @Override
    public void setActionPrice(Integer id, Double actionPrice, Integer until) {
        pharmacyMedicineRepository.setActionPrice(id, actionPrice, until);
    }

    @Override
    public void setActionPrice(Integer pharmacyId, PharmacyMedicineDTO medicineEdit) {
        //pharmacyMedicineService.setActionPrice(pharmed.getId(), medicineEdit.getActionPrice(), medicineEdit.getUntilDateTime());
        PharmacyMedicine pharmed = pharmacyMedicineRepository.getOne(medicineEdit.getId());
        pharmed.setActionPrice(medicineEdit.getActionPrice());
        pharmed.setUntilDateTime(medicineEdit.getUntilDateTime());
        List<PharmacyMedicinePrice> prices = pharmed.getMedicinePrices();
        if(prices.size() <= 0)
        {
            PharmacyMedicinePrice first = new PharmacyMedicinePrice(pharmed.getPrice(), 329681829000L, pharmed);
            prices.add(first);
        }
        if(medicineEdit.getActionPrice() == null && medicineEdit.getUntilDateTime() == null)
        {
            List<PharmedActionPrice> actionPrices = prices.get(prices.size()-1).getPharmedActionPrices();
            prices.get(prices.size()-1).getPharmedActionPrices().get(actionPrices.size()-1).setEndDate(System.currentTimeMillis());
            pharmed.setMedicinePrices(prices);
            pharmacyMedicineRepository.save(pharmed);
        }
        else
        {
            pharmed.setMedicinePrices(prices);
            pharmacyMedicineRepository.save(pharmed);
            PharmedActionPrice pharmedActionPrice = new PharmedActionPrice(medicineEdit.getActionPrice(), System.currentTimeMillis(),
                    medicineEdit.getUntilDateTime(), prices.get(prices.size()-1));
            actionPriceRepository.save(pharmedActionPrice);
        }
        //prices.get(prices.size()-1).addActionPrice(pharmedActionPrice);
        //sendNewPromotionMail(pharmacyId, medicineEdit, pharmed);
    }

    @Override
    public void updatePharmacyMedicine(PharmacyMedicineEdit medicineEdit) {
        PharmacyMedicine pharmed = pharmacyMedicineRepository.getOne(medicineEdit.getId());
        pharmed.setPrice(medicineEdit.getPrice());
        pharmed.setAvailableAmount(medicineEdit.getAvailable());
        pharmed.setActionPrice(null);
        pharmed.setUntilDateTime(null);

        List<PharmacyMedicinePrice> prices = pharmed.getMedicinePrices();
        if(prices.size() != 0) {
            PharmacyMedicinePrice price = prices.get(prices.size()-1);
            price.setEndDate(System.currentTimeMillis());
            prices.set(prices.size()-1, price);
        }

        PharmacyMedicinePrice price = new PharmacyMedicinePrice(medicineEdit.getPrice(),
                System.currentTimeMillis(), pharmed);
        prices.add(price);
        pharmed.setMedicinePrices(prices);
        pharmacyMedicineRepository.save(pharmed);
        //pharmedPriceService.save(price);
        //return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void promotionMail(PharmacyMedicineDTO medicineEdit, PharmacyMedicine pharmed,
                              List<Subscription> subscriptions, String pharmacyName) {
        List<javax.mail.Address> emailRej = new ArrayList<>();
        try {
            for (Subscription sub : subscriptions) {
                emailRej.add(new InternetAddress(sub.getPatient().getEmail()));
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        String title = "";
        String content = "";
        if (medicineEdit.getActionPrice() == null) {
            title = "Promotion finished for pharmacy '" + pharmacyName + "'";
            content = "Medicine '" + pharmed.getMedicine().getName() + "' is not on promotion any more. <br>His price now is "
                    + pharmed.getPrice();
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  HH:mm");
            Date date = new Date(medicineEdit.getUntilDateTime());
            title = "New promotion in pharmacy '" + pharmacyName + "'";
            content = "Medicine '" + pharmed.getMedicine().getName() + "' is on promotion for -" +
                    medicineEdit.getActionPrice() + "RSD- <br> If you need, hurry and reserve one until " +
                    format.format(date);
        }
        sendNewPromotionMail(title, content, emailRej);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void sendNewPromotionMail(String title, String content, List<javax.mail.Address> emailRej){
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
            message.setRecipients(Message.RecipientType.TO, emailRej.toArray(new javax.mail.Address[0]));
            message.setSubject(title);
            String nesto = MailConstants.getMailMessage(title, content);
            message.setContent(nesto, "text/html");
            //message.setContent(content, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void sendMedicineOutOfStockMail(MedicineInquiry mi, String recipient) {
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
            message.setSubject(mi.getMedicine().getName() + " out of stock!");
            String msg =
                            "Medicine id: " + mi.getMedicine().getId() +  "<br>" +
                            "Medicine name: " + mi.getMedicine().getName() + "<br>" +
                            "Manufacture: " + mi.getMedicine().getManufacture() + "<br>" +
                            "Pharmacy id: " + mi.getPharmacy().getId() + "<br>" +
                            "Pharmacy name: " + mi.getPharmacy().getName();


            message.setContent(msg, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> soldMonthly(List<MedicineReservationItem> items) {
        List<List<MedicineReservationItem>> separated = getLastYear(items);
        return soldStats(separated);
    }

    private List<Integer> soldYearly(List<MedicineReservationItem> items) {
        List<List<MedicineReservationItem>> separated = getLastTenYears(items);
        return soldStats(separated);
    }

    private List<Integer> soldStats(List<List<MedicineReservationItem>> separated) {
        List<Integer> calculated = new ArrayList<>();
        for(List<MedicineReservationItem> appList: separated) {
            Integer sum = 0;
            for (MedicineReservationItem item : appList)
                sum += item.getAmount();
            calculated.add(sum);
        }
        return calculated;
    }

    private List<List<MedicineReservationItem>> getLastYear(List<MedicineReservationItem> items) {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        Calendar start = Calendar.getInstance();
        start.add(Calendar.YEAR, -1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        int i = (start.get(Calendar.MONTH) + 1) %12;
        while(counter < 12) {
            map.put(i, counter);
            counter++;
            i++;
            if(i == 12)
                i = 0;
        }
        curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
        return filterList(start, curr, items, true, map);
    }

    private List<List<MedicineReservationItem>> getLastTenYears(List<MedicineReservationItem> items) {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        Calendar start = Calendar.getInstance();
        start.set(curr.get(Calendar.YEAR)-9, 0, 1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        for(int i = start.get(Calendar.YEAR); i <= curr.get(Calendar.YEAR); i++, counter++)
            map.put(i, counter);
        curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
        return filterList(start, curr, items, false, map);
    }

    private List<List<MedicineReservationItem>> filterList(Calendar start, Calendar end, List<MedicineReservationItem> items,
                                                           boolean month, Map<Integer, Integer> map) {
        List<List<MedicineReservationItem>> separated = new ArrayList<>();
        if(month) {
            for(int i = 0; i < 12; i++)
                separated.add(new ArrayList<>());
        }
        else {
            for(int i = 0; i < 10; i++)
                separated.add(new ArrayList<>());
        }

        for(MedicineReservationItem item: items) {
            Calendar curr = Calendar.getInstance();
            curr.setTimeInMillis(item.getMedicineReservation().getDateAndTime());
            if(!item.getMedicineReservation().getStatus().equals(MedicineReservationStatus.CANCELED)) {
                if (curr.after(start) && curr.before(end)) {
                    if (month)
                        separated.get(map.get(curr.get(Calendar.MONTH))).add(item);
                    else
                        separated.get(map.get(curr.get(Calendar.YEAR))).add(item);
                }
            }
        }
        return separated;
    }
}
