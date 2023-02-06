package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.repository.SupplierRepository;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.SupplierToSupplierDTO;
import pharmacyinformationsystem.web.dto.users.SupplierDTO;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class JpaSupplierService  implements SupplierService {
    private final SupplierRepository supplierRepository;

    private final AddressService addressService;
    private final RoleService roleService;
    private final OrderService orderService;
    private final OrderOfferService orderOfferService;

    @Autowired
    public JpaSupplierService(SupplierRepository supplierRepository, AddressService addressService, RoleService roleService, OrderService orderService, OrderOfferService orderOfferService) {
        this.supplierRepository = supplierRepository;
        this.addressService = addressService;
        this.roleService = roleService;
        this.orderService = orderService;
        this.orderOfferService = orderOfferService;
    }
    @Override
    @Transactional(readOnly = true)
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier findOne(Integer id) {
        return supplierRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Supplier save(Supplier entity) {
        return supplierRepository.save(entity);
    }

    @Override
    public List<Supplier> findSuppliersByFirstAndLastName(String firstAndLastName) {
        return supplierRepository.findSuppliersByFirstAndLastName(firstAndLastName);
    }

    @Override
    public Supplier findSupplierByEmail(String email) {
        return supplierRepository.findSupplierByEmail(email);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        if (findSupplierByEmail(supplier.getEmail()) != null) {
            return null;
        }

        Address address = addressService.save(supplier.getAddress());
        supplier.setAddress(address);
        supplier.setActive(true);
        Role role = roleService.findByName("ROLE_SUPPLIER");
        supplier.setRole(role);
        return save(supplier);
    }

    @Override
    public Page<Supplier> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return supplierRepository.findAll(pageable);
    }

    @Override
    public List<Supplier> supplierPageToList(Page<Supplier> suppliers) {
        return suppliers.toList();
    }

    @Override
    public OrderOffer makeOrderOffer(OrderOffer orderOffer, Integer id) {
        Supplier supplier = findOne(id);

        if (orderService.findOne(orderOffer.getOrder().getId()) == null) {
            return null;
        }

        Order order = orderService.findOne(orderOffer.getOrder().getId());

        if (supplier == null || order == null)
            return null;

        orderOffer.setOrder(order);
        orderOffer.setSupplier(supplier);
        orderOffer.setOrderOfferStatus(OrderOfferStatus.UNKNOWN);
        return orderOfferService.save(orderOffer);
    }

    @Override
    public Boolean updateOrderOffer(OrderOffer orderOffer) {
        OrderOffer oo = orderOfferService.findOne(orderOffer.getId());
        if (oo == null)
            return false;

        if(orderOffer.getDeliveryDeadline() == null) {
            return false;
        }

        if (orderOffer.getDeliveryDeadline() < oo.getOrder().getCreatedDate() ||
                orderOffer.getDeliveryDeadline() > oo.getOrder().getDeadline()) {
            return false;
        }

        oo.setDeliveryDeadline(orderOffer.getDeliveryDeadline());
        oo.setTotalPrice(orderOffer.getTotalPrice());
        orderOfferService.save(oo);
        return true;
    }

    @Override
    public List<OrderOffer> supplierOrderOffers(Integer id) {
        Supplier supplier = findOne(id);
        if (supplier == null)
            return null;

        return new ArrayList<>(supplier.getOrderOffers());
    }

}
