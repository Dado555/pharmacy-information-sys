package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.web.dto.domain.OrderOfferDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderOfferToOrderOfferDTO implements Converter<OrderOffer, OrderOfferDTO> {

    @Override
    public OrderOfferDTO convert(OrderOffer orderOffer) {
        OrderOfferDTO orderOfferDTO = new OrderOfferDTO(orderOffer.getId(), orderOffer.getTotalPrice(), orderOffer.getDeliveryDeadline(),
                orderOffer.getOrderOfferStatus(), orderOffer.getOrder().getId(), 0);
        Supplier supplier = orderOffer.getSupplier();
        if (supplier != null) {
            orderOfferDTO.setSupplierId(supplier.getId());
        }
        return orderOfferDTO;
    }

    public List<OrderOfferDTO> convert(List<OrderOffer> orderOfferList) {
        return orderOfferList.stream().map(this::convert).collect(Collectors.toList());
    }
}
