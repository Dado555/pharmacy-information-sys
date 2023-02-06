package pharmacyinformationsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;
import pharmacyinformationsystem.model.users.Supplier;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class OrderOffer extends BaseEntity {

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "delivery_deadline", nullable = false)
    private Long deliveryDeadline;

    @Column(name = "order_offer_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderOfferStatus orderOfferStatus;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore
    private Supplier supplier;

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        if (this.supplier != null && !this.supplier.getOrderOffers().contains(this))
            supplier.addOrderOffer(this);
    }
}
