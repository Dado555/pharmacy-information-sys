package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="orderItems")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class OrderItem extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
