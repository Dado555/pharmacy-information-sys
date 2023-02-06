package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderItem;

public interface OrderItemService extends GenericService<OrderItem>{
    void deleteItem(Integer id);

    OrderItem findOne(Order order, Medicine medicine);
}
