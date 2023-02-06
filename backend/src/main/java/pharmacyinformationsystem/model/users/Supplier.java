package pharmacyinformationsystem.model.users;

import lombok.Getter;
import lombok.Setter;
import pharmacyinformationsystem.model.OrderOffer;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity @Getter @Setter
public class Supplier extends User {

    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderOffer> orderOffers;

    public Supplier() {
        super();
    }

    public Supplier addOrderOffer(OrderOffer orderOffer) {
        this.orderOffers.add(orderOffer);
        if (!this.equals(orderOffer.getSupplier()))
            orderOffer.setSupplier(this);
        return this;
    }
}
