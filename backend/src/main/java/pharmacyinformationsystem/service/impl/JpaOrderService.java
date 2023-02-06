package pharmacyinformationsystem.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;
import pharmacyinformationsystem.model.enums.OrderStatus;
import pharmacyinformationsystem.repository.OrderOfferRepository;
import pharmacyinformationsystem.repository.OrderRepository;
import pharmacyinformationsystem.repository.PharmacyMedicineRepository;
import pharmacyinformationsystem.service.OrderService;
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
public class JpaOrderService implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderOfferRepository offerRepository;
    private final PharmacyMedicineRepository pharmacyMedicineRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public JpaOrderService(OrderRepository orderRepository, OrderOfferRepository offerRepository, PharmacyMedicineRepository pharmacyMedicineRepository) {
        this.orderRepository = orderRepository;
        this.offerRepository = offerRepository;
        this.pharmacyMedicineRepository = pharmacyMedicineRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOne(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public void delete(Integer id) {
        this.orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findByStatus(OrderStatus orderStatus, Pharmacy pharmacy) {
        return orderRepository.findOrdersByOrderStatusAndPharmacy(orderStatus, pharmacy);
    }

    @Override
    public List<OrderOffer> getOffers(Integer orderId) {
        Order order = orderRepository.getOne(orderId);
        return new ArrayList<>(order.getOrderOfferList());
    }

    @Override
    public Order getOneOrder(Integer id) {
        Order found = orderRepository.getOne(id);
        Order order = new Order();
        order.setId(found.getId());
        order.setOrderStatus(found.getOrderStatus());
        order.setDeadline(found.getDeadline());
        order.setCreatedDate(found.getCreatedDate());
        order.setPharmacy(found.getPharmacy());
        order.setPharmacyAdmin(found.getPharmacyAdmin());
        order.setOrderItems(new ArrayList<>(found.getOrderItems()));
        order.setOrderOfferList(found.getOrderOfferList());
        order.setActive(found.getActive());
        return order;
    }

    @Override
    public List<OrderItem> getOrderItems(Integer id) {
        Order order = orderRepository.getOne(id);
        return new ArrayList<>(order.getOrderItems());
    }

    @Override
    public void acceptOffer(Integer orderId, Integer offerId) {
        Order order = entityManager.find(Order.class, orderId); //orderRepository.getOne(orderId);
        entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
        List<PharmacyMedicine> pharmacyMedicines = order.getPharmacy().getPharmacyMedicineList();
        for(OrderItem item: order.getOrderItems()) {
            boolean found = false;
            for(PharmacyMedicine medicine : pharmacyMedicines) {
                if(medicine.getMedicine().getId().equals(item.getMedicine().getId())) {
                    found = true;
                    PharmacyMedicine medicine1 = entityManager.find(PharmacyMedicine.class, medicine.getId());
                    entityManager.lock(medicine1, LockModeType.PESSIMISTIC_WRITE);
                    medicine1.setAvailableAmount(medicine.getAvailableAmount() + item.getAmount());
                }
            }
            if(!found) {
                PharmacyMedicine pharmacyMedicine = new PharmacyMedicine();
                pharmacyMedicine.setPharmacy(order.getPharmacy());
                pharmacyMedicine.setMedicine(item.getMedicine());
                pharmacyMedicine.setAvailableAmount(item.getAmount());
                pharmacyMedicineRepository.save(pharmacyMedicine);
                pharmacyMedicines.add(pharmacyMedicine);
            }
        }
        order.setOrderStatus(OrderStatus.DONE);

        //order.getPharmacy().setPharmacyMedicineList(pharmacyMedicines);
        orderRepository.save(order);
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void acceptAndSendMail(Integer orderId, Integer offerId, List<OrderItem> items, List<OrderOffer> offers) {
        StringBuilder content = new StringBuilder("Order content: ");
        //List<OrderItem> items = new ArrayList<>(order.getOrderItems());
        for(int i = 0; i < items.size(); i++){
            if(i == items.size()-1)
                content.append(items.get(i).getMedicine().getName()).append(". ");
            else
                content.append(items.get(i).getMedicine().getName()).append(", ");
        }
        List<javax.mail.Address> emailRej = new ArrayList<>();

        try {
            OrderOffer acceptOffer = null;
            for (OrderOffer offer : offers) {
                content.append("<br> Price: ").append(offer.getTotalPrice());
                if (offer.getId().equals(offerId)) {
                    acceptOffer = offer;
                    offer.setOrderOfferStatus(OrderOfferStatus.APPROVED);
                } else {
                    emailRej.add(new InternetAddress(offer.getSupplier().getEmail()));
                    offer.setOrderOfferStatus(OrderOfferStatus.REJECTED);
                }
                offerRepository.save(offer);
            }
            assert acceptOffer != null;
            sendAcceptedOfferMail(orderId, content, acceptOffer, emailRej);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    @Transactional(propagation = Propagation.NEVER)
    public void sendAcceptedOfferMail(Integer orderId, StringBuilder content,
                                      OrderOffer accept, List<javax.mail.Address> emailRej) throws MessagingException {
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

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(accept.getSupplier().getEmail()));
        message.setSubject("Your offer for order " + orderId + " has been accepted");
        String nesto = MailConstants.getMailMessage("Your offer for order " + orderId + " has been accepted",
                content.toString());
        message.setContent(nesto, "text/html");
        Transport.send(message);

        if(emailRej.size() > 0) {
            try {
                Message messageRej = new MimeMessage(session);
                messageRej.setFrom(new InternetAddress(email));
                messageRej.setRecipients(Message.RecipientType.TO, emailRej.toArray(new javax.mail.Address[0]));
                messageRej.setSubject("Your offer has been rejected");
                nesto = MailConstants.getMailMessage("Your offer for order " + orderId + " has been rejected",
                        content.toString());
                messageRej.setContent(nesto, "text/html");
                Transport.send(messageRej);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
