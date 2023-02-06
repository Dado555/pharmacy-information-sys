package pharmacyinformationsystem.web.dto.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class OrderOfferDTO {
    private Integer id;
    private Double totalPrice;
    private Long deliveryDeadline;
    private OrderOfferStatus orderOfferStatus;
    private Integer orderId;
    private Integer supplierId;
}
