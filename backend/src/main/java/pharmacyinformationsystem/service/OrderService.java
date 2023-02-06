package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderItem;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.OrderStatus;

import javax.mail.MessagingException;
import java.util.List;

public interface OrderService extends GenericService<Order> {
    void delete(Integer id);

    List<Order> findByStatus(OrderStatus orderStatus, Pharmacy pharmacy);

    List<OrderOffer> getOffers(Integer orderId);

    Order getOneOrder(Integer id);

    List<OrderItem> getOrderItems(Integer id);

    void acceptOffer(Integer orderId, Integer offerId);

    void sendAcceptedOfferMail(Integer orderId, StringBuilder content,
                               OrderOffer accept, List<javax.mail.Address> emailRej) throws MessagingException;

    void acceptAndSendMail(Integer orderId, Integer offerId, List<OrderItem> items, List<OrderOffer> offers);
}
