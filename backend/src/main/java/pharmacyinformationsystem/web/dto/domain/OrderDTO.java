package pharmacyinformationsystem.web.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pharmacyinformationsystem.model.enums.OrderStatus;

import javax.persistence.criteria.Order;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class OrderDTO {
    private Integer id;
    private Long deadline;
    private Long createdDate;
    private Integer pharmacyAdminId;
    private List<OrderItemDTO> orderItems;
    private OrderStatus orderStatus;
    private Integer pharmacyId;

    public OrderDTO(Integer id, Long deadline, Long createdDate, Integer phAdminId) {
        this.id = id;
        this.deadline = deadline;
        this.createdDate = createdDate;
        this.pharmacyAdminId = phAdminId;
    }
}
