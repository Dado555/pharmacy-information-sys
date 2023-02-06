package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.repository.OrderOfferRepository;
import pharmacyinformationsystem.service.OrderOfferService;
import pharmacyinformationsystem.service.base.OrderOfferServiceBase;
import pharmacyinformationsystem.web.dto.searchparameters.OrderOfferSearchParameters;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class JpaOrderOfferService extends OrderOfferServiceBase implements OrderOfferService {

    private final OrderOfferRepository orderOfferRepository;

    @Autowired
    public JpaOrderOfferService(OrderOfferRepository orderOfferRepository) {
        this.orderOfferRepository = orderOfferRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderOffer> searchByCriteria(OrderOfferSearchParameters searchParameters) {
        if (!searchParameters.getFilterBy().equals("") && searchParameters.getFilterBy() != null)
            return orderOfferRepository.filterByStatus(OrderOfferStatus.valueOf(searchParameters.getFilterBy()));

        return null;
    }

    @Override
    public List<OrderOffer> getByOrder(Order order) {
        return orderOfferRepository.getOrderOffersByOrder(order);
    }

    @Override
    public List<OrderOffer> findAll() {
        return orderOfferRepository.findAll();
    }

    @Override
    public OrderOffer findOne(Integer id) {
        return orderOfferRepository.findById(id).orElse(null);
    }

    @Override
    public OrderOffer save(OrderOffer entity) {
        return orderOfferRepository.save(entity);
    }

}
