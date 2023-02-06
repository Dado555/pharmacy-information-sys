package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderItem;
import pharmacyinformationsystem.repository.OrderItemRepository;
import pharmacyinformationsystem.service.OrderItemService;

import java.util.List;

@Service
public class JpaOrderItemService implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public JpaOrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findOne(Integer id) {
        return orderItemRepository.findById(id).orElse(null);
    }

    @Override
    public OrderItem save(OrderItem entity) {
        return orderItemRepository.save(entity);
    }

    @Override
    public void deleteItem(Integer id) {
        this.orderItemRepository.deleteById(id);
    }

    @Override
    public OrderItem findOne(Order order, Medicine medicine) {
        return this.orderItemRepository.findOrderItemByOrderAndMedicine(order, medicine);
    }
}
