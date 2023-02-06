package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pharmacyinformationsystem.model.enums.OrderStatus;
import pharmacyinformationsystem.model.users.PharmacyAdmin;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="orders")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Order extends BaseEntity {

    @Column(name = "deadline", nullable = false)
    private Long deadline;

    @Column(name = "created_date", nullable = false)
    private Long createdDate;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "pharmacy_admin_id")
    private PharmacyAdmin pharmacyAdmin;

    @ManyToOne
    @JoinColumn(name="pharmacy_id")
    private Pharmacy pharmacy;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderOffer> orderOfferList;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Version
    @ColumnDefault("0")
    private Short version;

    public Order(Integer id) {
        this.setId(id);
    }
}
