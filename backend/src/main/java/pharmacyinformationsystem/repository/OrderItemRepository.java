package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderItem;

import javax.transaction.Transactional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Modifying
    @Transactional
    void deleteById(Integer id);

    OrderItem findOrderItemByOrderAndMedicine(Order order, Medicine medicine);
}
