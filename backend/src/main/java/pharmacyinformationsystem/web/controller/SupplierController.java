package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.OrderOfferStatus;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.OrderOfferToOrderOfferDTO;
import pharmacyinformationsystem.support.OrderToOrderDTO;
import pharmacyinformationsystem.support.SupplierToSupplierDTO;
import pharmacyinformationsystem.support.UserToUserDTO;
import pharmacyinformationsystem.web.dto.PharmacyMedicineEdit;
import pharmacyinformationsystem.web.dto.domain.OrderDTO;
import pharmacyinformationsystem.web.dto.domain.OrderOfferDTO;
import pharmacyinformationsystem.web.dto.pages.MedicinePageDTO;
import pharmacyinformationsystem.web.dto.pages.PharmacyAdminPageDTO;
import pharmacyinformationsystem.web.dto.pages.SupplierPageDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;
import pharmacyinformationsystem.web.dto.searchparameters.OrderOfferSearchParameters;
import pharmacyinformationsystem.web.dto.users.SupplierDTO;
import pharmacyinformationsystem.web.dto.users.UserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    private final UserToUserDTO userToUserDTO;
    private final AddressService addressService;
    private final SupplierToSupplierDTO supplierToSupplierDTO;
    private final OrderOfferToOrderOfferDTO orderOfferToOrderOfferDTO;
    private final OrderToOrderDTO orderToOrderDTO;
    private final OrderOfferService orderOfferService;
    private final OrderService orderService;
    private final RoleService roleService;

    @Autowired
    public SupplierController(SupplierService supplierService, UserToUserDTO userToUserDTO, AddressService addressService, SupplierToSupplierDTO supplierToSupplierDTO, OrderOfferToOrderOfferDTO orderOfferToOrderOfferDTO, OrderToOrderDTO orderToOrderDTO, OrderOfferService orderOfferService, OrderService orderService, RoleService roleService) {
        this.supplierService = supplierService;
        this.userToUserDTO = userToUserDTO;
        this.addressService = addressService;
        this.supplierToSupplierDTO = supplierToSupplierDTO;
        this.orderOfferToOrderOfferDTO = orderOfferToOrderOfferDTO;
        this.orderToOrderDTO = orderToOrderDTO;
        this.orderOfferService = orderOfferService;
        this.orderService = orderService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getSuppliers() {
        List<User> suppliers = new ArrayList<User>(supplierService.findAll());
        return new ResponseEntity<>(userToUserDTO.convert(suppliers), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getOneSupplier(@PathVariable("id") Integer id) {
        User user = supplierService.findOne(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(userToUserDTO.convert(user), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody Supplier supplier) {

        if (supplierService.findSupplierByEmail(supplier.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Address address = addressService.save(supplier.getAddress());
        supplier.setAddress(address);
        supplier.setActive(true);
        Role role = roleService.findByName("ROLE_SUPPLIER");
        supplier.setRole(role);
        Supplier savedSupplier = supplierService.save(supplier);
        if (savedSupplier == null)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(supplierToSupplierDTO.convert(savedSupplier), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SUPPLIER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateSupplier(@RequestBody Supplier supplier, @PathVariable Integer id) {
        Supplier user = supplierService.findOne(id);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        user.update(supplier.getFirstName(), supplier.getLastName(), supplier.getPhoneNumber());

        Address address = addressService.findOne(user.getAddress().getId());
        if (address == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        address.update(supplier.getAddress().getCity(), supplier.getAddress().getName(), supplier.getAddress().getNumber(),
                supplier.getAddress().getPostalCode());
        addressService.save(address);

        return new ResponseEntity<>(userToUserDTO.convert(supplierService.save(user)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deleteSupplier(@PathVariable("id") Integer id) {
        User u = supplierService.findOne(id);
        if (u == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        u.setActive(false);
        return new ResponseEntity<>(userToUserDTO.convert(u), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SUPPLIER')")
    @GetMapping(value = "/{id}/order-offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderOfferDTO>> getOrderOffers(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderOfferToOrderOfferDTO.convert(supplierService.supplierOrderOffers(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SUPPLIER')")
    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getOrders() {

        List<Order> orders = new ArrayList<>(orderService.findAll());
        return new ResponseEntity<>(orderToOrderDTO.convert(orders), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'SUPPLIER')")
    @PostMapping(value = "/{id}/make-order-offer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderOfferDTO> makeOrderOffer(@RequestBody OrderOffer orderOffer, @PathVariable("id") Integer id) {
        return new ResponseEntity<>(orderOfferToOrderOfferDTO.convert(supplierService.makeOrderOffer(orderOffer, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPPLIER')")
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderOfferDTO>> searchMedicines(@RequestBody OrderOfferSearchParameters searchParameters) {
        List<OrderOffer> orderOffers = orderOfferService.searchByCriteria(searchParameters);
        return new ResponseEntity<>(orderOfferToOrderOfferDTO.convert(orderOffers), HttpStatus.OK);
    }

    @PutMapping(value = "/update-order-offer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateOrderOffer(@RequestBody OrderOffer orderOffer) {
        return new ResponseEntity<>(supplierService.updateOrderOffer(orderOffer), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierPageDTO> getSupplierPage(@RequestParam Optional<Integer> page) {
        Page<Supplier> suppliers = supplierService.findAll(page.orElse(0));
        return new ResponseEntity<>(new SupplierPageDTO(supplierToSupplierDTO.convert(supplierService.supplierPageToList(suppliers)),
                suppliers.getTotalPages()) , HttpStatus.OK);
    }
}
