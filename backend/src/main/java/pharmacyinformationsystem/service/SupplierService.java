package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.web.dto.users.SupplierDTO;

import java.util.List;

public interface SupplierService extends GenericService<Supplier>{
    List<Supplier> findSuppliersByFirstAndLastName(String firstAndLastName);

    Supplier findSupplierByEmail(String email);

    Supplier createSupplier(Supplier supplier);

    Page<Supplier> findAll(Integer page);

    List<Supplier> supplierPageToList(Page<Supplier> suppliers);

    OrderOffer makeOrderOffer(OrderOffer orderOffer, Integer id);

    Boolean updateOrderOffer(OrderOffer orderOffer);

    List<OrderOffer> supplierOrderOffers(Integer id);
}
