package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.web.dto.searchparameters.OrderOfferSearchParameters;

import java.util.List;

public interface OrderOfferService extends GenericService<OrderOffer> {

    List<OrderOffer> searchByCriteria(OrderOfferSearchParameters searchParameters);

    List<OrderOffer> getByOrder(Order order);
}
