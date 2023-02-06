package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;

import java.util.List;

@Repository
public interface OrderOfferRepository extends JpaRepository<OrderOffer, Integer> {

    @Query("select o from OrderOffer o where o.orderOfferStatus = ?1")
    List<OrderOffer> filterByStatus(OrderOfferStatus orderOfferStatus);

    List<OrderOffer> getOrderOffersByOrder(Order order);
}
