package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.enums.OrderStatus;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Modifying
    @Transactional
    void deleteById(Integer id);

    List<Order> findOrdersByOrderStatusAndPharmacy(OrderStatus orderStatus, Pharmacy pharmacy);
}
