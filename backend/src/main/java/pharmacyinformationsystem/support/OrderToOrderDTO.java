package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderItem;
import pharmacyinformationsystem.service.OrderOfferService;
import pharmacyinformationsystem.service.OrderService;
import pharmacyinformationsystem.web.dto.domain.OrderDTO;
import pharmacyinformationsystem.web.dto.domain.OrderItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderToOrderDTO implements Converter<Order, OrderDTO> {
    private final MedicineToMedicineDTO medicineToMedicineDTO;
    private final OrderService orderService;

    @Autowired
    public OrderToOrderDTO(MedicineToMedicineDTO medicineDTO, OrderService orderService) {
        this.medicineToMedicineDTO = medicineDTO;
        this.orderService = orderService;
    }

    @Override
    public OrderDTO convert(Order order) {
        Order order1 = orderService.findOne(order.getId());
        return new OrderDTO(order1.getId(), order1.getDeadline(), order1.getCreatedDate(),
                order1.getPharmacyAdmin().getId(), convertItems(orderService.getOrderItems(order1.getId())),
                order1.getOrderStatus(), order1.getPharmacy().getId());
    }

    public OrderDTO convertWithoutList(Order order) {
        return new OrderDTO(order.getId(), order.getDeadline(), order.getCreatedDate(),
                order.getPharmacyAdmin().getId(), null, order.getOrderStatus(), order.getPharmacy().getId());
    }

    public List<OrderDTO> convert(List<Order> orderList) {
        return orderList.stream().map(this::convert).collect(Collectors.toList());
    }

    public List<OrderDTO> convertWithoutList(List<Order> orderList) {
        return orderList.stream().map(this::convertWithoutList).collect(Collectors.toList());
    }

    private List<OrderItemDTO> convertItems(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::convert).collect(Collectors.toList());
    }

    private OrderItemDTO convert(OrderItem item) {
        return new OrderItemDTO(item.getId(), medicineToMedicineDTO.convert(item.getMedicine()), item.getAmount());
    }
}
