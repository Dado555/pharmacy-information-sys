package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.OrderOffer;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.service.OrderOfferService;
import pharmacyinformationsystem.service.SupplierService;
import pharmacyinformationsystem.web.dto.users.SupplierDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierToSupplierDTO implements Converter<Supplier, SupplierDTO> {
    private final AddressToAddressDTO addressToAddressDTO;
    private final OrderOfferService orderOfferService;
    private final SupplierService supplierService;
    private final OrderOfferToOrderOfferDTO orderOfferToOrderOfferDTO;

    @Autowired
    public SupplierToSupplierDTO(AddressToAddressDTO addressToAddressDTO, OrderOfferService orderOfferService, SupplierService supplierService, OrderOfferToOrderOfferDTO orderOfferToOrderOfferDTO) {
        this.addressToAddressDTO = addressToAddressDTO;
        this.orderOfferService = orderOfferService;
        this.supplierService = supplierService;
        this.orderOfferToOrderOfferDTO = orderOfferToOrderOfferDTO;
    }

    @Override
    public SupplierDTO convert(Supplier supplier) {
        return new SupplierDTO(supplier.getId(),
                supplier.getEmail(),
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getPhoneNumber(),
                addressToAddressDTO.convert(supplier.getAddress()),
                supplier.getRole().getName(),
                supplier.getActive());
    }

    public List<SupplierDTO> convert(List<Supplier> supplierList) {
        List<SupplierDTO> supplierDTOList = new ArrayList<SupplierDTO>();
        for (Supplier supplier : supplierList) {
            SupplierDTO supplierDTO = this.convert(supplier);
            for (OrderOffer orderOffer : supplierService.supplierOrderOffers(supplier.getId())) {
                supplierDTO.setOrderOfferDTO(orderOfferToOrderOfferDTO.convert(orderOffer));
            }
            supplierDTOList.add(supplierDTO);
        }
        return supplierDTOList;
    }
}
