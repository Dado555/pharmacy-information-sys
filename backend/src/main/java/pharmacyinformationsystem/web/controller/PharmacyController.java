package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Pharmacy;

import pharmacyinformationsystem.model.enums.OrderStatus;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.service.AppointmentService;
import pharmacyinformationsystem.service.AddressService;

import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.service.*;

import pharmacyinformationsystem.web.dto.PharmacistDTO;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.PharmacyDTO;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;
import pharmacyinformationsystem.model.*;

import pharmacyinformationsystem.support.*;
import pharmacyinformationsystem.web.dto.domain.*;
import pharmacyinformationsystem.web.dto.pages.PharmacyPageDTO;
import pharmacyinformationsystem.web.dto.users.DermatologistDTO;
import pharmacyinformationsystem.web.dto.searchparameters.PharmacySearchParameters;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final MedicineService medicineService;
    private final PharmacyAdminService pharmacyAdminService;
    private final AppointmentService appointmentService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final OrderOfferService orderOfferService;
    private final PharmacytoPharmacyDTO toPharmacyDTO;
    private final AppointmentToAppointmentDTO toAppointmentDTO;
    private final PharmacyMedicineToPharmacyMedicineDTO toPharmacyMedicineDTO;
    private final PharmacistToPharmacistDTO toPharmacistDTO;
    private final DermatologistToDermatologistDTO toDermatologistDTO;
    private final AddressService addressService;
    private final OrderToOrderDTO toOrderDTO;
    private final OrderOfferToOrderOfferDTO toOrderOfferDTO;
    private final TimeOffToTimeOffDTO timeOffToTimeOffDTO;
    private final TimeOffRequestService timeOffRequestService;
    private final MedicineInquiryToMedicineInquiryDTO medicineInquiryToMedicineInquiryDTO;
    private final MedicineInquiryService medicineInquiryService;

    @Autowired
    public PharmacyController(PharmacyService pharmacyService, AppointmentService appointmentService,
                              AppointmentToAppointmentDTO toAppointmentDTO, PharmacytoPharmacyDTO toPharmacyDTO,
                              PharmacyMedicineToPharmacyMedicineDTO toPharmacyMedicineDTO, PharmacyAdminService pharmacyAdminService,
                              PharmacistToPharmacistDTO toPharmacistDTO, DermatologistToDermatologistDTO toDermatologistDTO,
                              AddressService addressService, OrderToOrderDTO toOrderDTO, OrderOfferToOrderOfferDTO toOrderOfferDTO,
                              OrderItemService orderItemService, OrderService orderService, OrderOfferService orderOfferService,
                              MedicineService medicineService, TimeOffToTimeOffDTO timeOffToTimeOffDTO, TimeOffRequestService timeOffRequestService,
                              MedicineInquiryToMedicineInquiryDTO medicineInquiryToMedicineInquiryDTO, MedicineInquiryService medicineInquiryService) {
        this.pharmacyService = pharmacyService;
        this.pharmacyAdminService = pharmacyAdminService;
        this.appointmentService = appointmentService;
        this.toAppointmentDTO = toAppointmentDTO;
        this.toPharmacyDTO = toPharmacyDTO;
        this.toPharmacyMedicineDTO = toPharmacyMedicineDTO;
        this.toPharmacistDTO = toPharmacistDTO;
        this.toDermatologistDTO = toDermatologistDTO;
        this.addressService = addressService;
        this.toOrderDTO = toOrderDTO;
        this.toOrderOfferDTO = toOrderOfferDTO;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.orderOfferService = orderOfferService;
        this.medicineService = medicineService;
        this.timeOffToTimeOffDTO = timeOffToTimeOffDTO;
        this.timeOffRequestService = timeOffRequestService;
        this.medicineInquiryToMedicineInquiryDTO = medicineInquiryToMedicineInquiryDTO;
        this.medicineInquiryService = medicineInquiryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyPageDTO> getPharmacies(@RequestParam Optional<Integer> page) {
        Page<Pharmacy> pharmacies = pharmacyService.findAll(page.orElse(0));
        return new ResponseEntity<>(new PharmacyPageDTO(toPharmacyDTO.convert(pharmacies.toList()), pharmacies.getTotalPages()), HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyDTO>> getPharmacies() {
        List<Pharmacy> pharmacies = pharmacyService.findAll();
        return new ResponseEntity<>(toPharmacyDTO.convert(pharmacies), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> getOnePharmacy(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if (pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        System.out.println(pharmacy.getName() + ": " + pharmacy.getStartWorkTime() + "h - " + pharmacy.getEndWorkTime() + "h");
        PharmacyDTO pharDTO = toPharmacyDTO.convert(pharmacy);
        assert pharDTO != null;
        System.out.println(pharDTO.getName() + ": " + pharDTO.getStartWorkTime() + "h - " + pharDTO.getEndWorkTime()+ "h");
        return new ResponseEntity<>(toPharmacyDTO.convert(pharmacy), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getPharmacyRatingsMonth(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacyService.getMonthlyRatings(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/ratings-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getPharmacyRatingsYear(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacyService.getYearlyRatings(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT', 'PHARMACIST', 'DERMATOLOGIST')")
    @GetMapping(value = "/{id}/medicines", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyMedicineDTO>> getMedicines(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(toPharmacyMedicineDTO.convert(pharmacyService.getPharmacyMedicines(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/dermatologists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DermatologistDTO>> getDermatologists(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(toDermatologistDTO.convert(pharmacyService.getDermatologists(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/pharmacists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacistDTO>> getPharmacists(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(toPharmacistDTO.convert(pharmacyService.getPharmacists(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'PATIENT')")
    @GetMapping(value = "/{id}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppointmentDTO>> getFreeAppointments(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(toAppointmentDTO.convert(appointmentService.findFreeAppointments(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(toOrderDTO.convertWithoutList(pharmacyService.getOrders(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN', 'SUPPLIER')")
    @GetMapping(value = "/{id}/order-{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrders(@PathVariable("id") Integer id, @PathVariable("orderId") Integer orderId) {
        Order order = orderService.findOne(orderId);
        if(order == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(toOrderDTO.convert(orderService.getOneOrder(orderId)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/orders/status-{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable("id") Integer id, @PathVariable("status") String status) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(status.equals("NONE"))
            return new ResponseEntity<>(toOrderDTO.convertWithoutList(pharmacyService.getOrders(id)), HttpStatus.OK);
        return new ResponseEntity<>(toOrderDTO.convertWithoutList(orderService.findByStatus(OrderStatus.valueOf(status), pharmacy)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/order-{orderId}/offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderOfferDTO>> getOrderOffers(@PathVariable("orderId") Integer orderId) {
        Order order = orderService.findOne(orderId);
        if(order == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(toOrderOfferDTO.convert(orderOfferService.getByOrder(order)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{orderId}/update-order-item-{orderItemId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateOrderItem(@RequestBody OrderItemDTO orderItemDTO,
                                                   @PathVariable("orderId") Integer orderId, @PathVariable("orderItemId") Integer id) {
        OrderItem p = orderItemService.findOne(id);
        Order o = orderService.findOne(orderId);
        if (p == null || o == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        p.setAmount(orderItemDTO.getAmount());
        orderItemService.save(p);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{orderId}/new-order-item", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addOrderItem(@RequestBody OrderItemDTO orderItemDTO, @PathVariable("orderId") Integer orderId) {
        Order p = orderService.findOne(orderId);
        Medicine m = medicineService.findOne(orderItemDTO.getMedicineDTO().getId());
        if (p == null || m == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        OrderItem item = orderItemService.findOne(p, m);
        if(item == null) {
            item = new OrderItem();
            item.setMedicine(m);
            item.setOrder(p);
            item.setAmount(orderItemDTO.getAmount());
        } else {
            item.setAmount(item.getAmount() + orderItemDTO.getAmount());
        }
        orderItemService.save(item);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @DeleteMapping(value = "/{orderId}/delete-order-item-{item}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteOrderItem(@PathVariable("orderId") Integer orderId, @PathVariable("item") Integer itemId) {
        Order p = orderService.findOne(orderId);
        OrderItem item = orderItemService.findOne(itemId);
        if (p == null || item == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        orderItemService.deleteItem(itemId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @DeleteMapping(value = "/order-{orderId}/delete-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderId") Integer orderId) {
        Order p = orderService.findOne(orderId);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        for(OrderItem item: orderService.getOrderItems(orderId)){
            orderItemService.deleteItem(item.getId());
        }
        orderService.delete(p.getId());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/make-order", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> makeOrder(@RequestBody OrderDTO orderDTO) {
        Pharmacy pharmacy = pharmacyService.findOne(orderDTO.getPharmacyId());
        PharmacyAdmin pharmacyAdmin = pharmacyAdminService.findOne(orderDTO.getPharmacyAdminId());
        if(pharmacy == null || pharmacyAdmin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Order order = new Order();
        order.setPharmacy(pharmacy);
        order.setPharmacyAdmin(pharmacyAdmin);
        order.setDeadline(orderDTO.getDeadline());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setCreatedDate(System.currentTimeMillis());
        orderService.save(order);
        for(OrderItemDTO item: orderDTO.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setAmount(item.getAmount());
            orderItem.setMedicine(medicineService.findOne(item.getMedicineDTO().getId()));
            orderItemService.save(orderItem);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/order-{orderId}/get-offers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderOfferDTO>> getOffers(@PathVariable("orderId") Integer orderId) {
        Order order = orderService.findOne(orderId);
        if(order == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(toOrderOfferDTO.convert(orderService.getOffers(orderId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/order-{orderId}/accept-offer-{offerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> acceptOffer(@PathVariable("orderId") Integer orderId, @PathVariable("offerId") Integer offerId) {
        Order order = orderService.findOne(orderId);
        if(order == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        orderService.acceptOffer(orderId, offerId);
        orderService.acceptAndSendMail(orderId, offerId, orderService.getOrderItems(orderId), orderService.getOffers(orderId));

        //orderService.sendAcceptedOfferMail(orderService.findOne(orderId), new StringBuilder(), offerId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{pharId}/time-off-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeOffDTO>> getTimeOffs(@PathVariable("pharId") Integer pharId) {
        Pharmacy pharmacy = pharmacyService.findOne(pharId);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(timeOffToTimeOffDTO.convert(pharmacyService.getTimeOffs(pharId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{pharId}/medicine-inquiries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MedicineInquiryDTO>> getInquiries(@PathVariable("pharId") Integer pharId) {
        Pharmacy pharmacy = pharmacyService.findOne(pharId);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(medicineInquiryToMedicineInquiryDTO.convert(pharmacyService.getMedicineInquiries(pharId)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/medicine-inquiry-{inqId}/resolve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> resolveInquiry(@PathVariable("inqId") Integer inqId) {
        MedicineInquiry medicineInquiry = medicineInquiryService.findOne(inqId);
        if(medicineInquiry == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        medicineInquiry.setResolved(true);
        medicineInquiryService.save(medicineInquiry);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/time-off-request-{reqId}/status-{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> approveTimeOff(@PathVariable("reqId") Integer reqId, @PathVariable("status") Boolean status) {
        TimeOffRequest timeOffRequest = timeOffRequestService.findOne(reqId);
        if(timeOffRequest == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        timeOffRequestService.approveTimeOff(reqId, status);
        timeOffRequestService.approveTimeOffRequestEmail(timeOffRequestService.findOne(reqId), status);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyDTO>> getPharmaciesByDateAndTimeCriteria(@RequestParam("startDate") Long startDate, @RequestParam("startTime") Long startTime, @RequestParam("endTime") Long endTime) {
        return new ResponseEntity<>(toPharmacyDTO.convert(pharmacyService.findAllByDateAndTimeCriteria(startDate, startTime, endTime)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping(value = "/{id}/free", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacistDTO>> getPharmacistsByDateAndTime(@PathVariable("id") Integer id, @RequestParam("start") Long start, @RequestParam("end") Long end) {
        return new ResponseEntity<>(toPharmacistDTO.convert(pharmacyService.findPharmacistsByDateAndTimeCriteria(id, start, end)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/earnings-monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getEarningsMonthly(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacyService.getMonthlyEarnings(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/earnings-yearly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Double>> getEarningsYearly(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacyService.getYearlyEarnings(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}/earnings-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Double> getEarningsAll(@PathVariable("id") Integer id) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pharmacyService.getAllEarnings(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> createPharmacy(@RequestBody Pharmacy pharmacy) {
        return new ResponseEntity<>(toPharmacyDTO.convert(pharmacyService.savePharmacy(pharmacy)), HttpStatus.OK);
    }

    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyPageDTO> searchPharmacies(@RequestParam Optional<Integer> page, @RequestBody PharmacySearchParameters searchParameters) {
        Page<Pharmacy> pharmacies = pharmacyService.searchByCriteria(searchParameters, Optional.of(page.orElse(0)));
        return new ResponseEntity<>(new PharmacyPageDTO(toPharmacyDTO.convert(pharmacies.toList()), pharmacies.getTotalPages()), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> updatePharmacy(@RequestBody Pharmacy pharmacy, @PathVariable Integer id) {
        Pharmacy p = pharmacyService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        p.setName(pharmacy.getName());
        p.setAddress(pharmacy.getAddress());
        return new ResponseEntity<>(toPharmacyDTO.convert(p), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{id}/name-{new}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> updatePharmacyName(@PathVariable("id") Integer id, @PathVariable("new") String newName) {
        Pharmacy p = pharmacyService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        System.out.println("ID APOTEKE" + id);
        System.out.println("Novo ime: " + newName);
        pharmacyService.updateName(id, newName);
        p.setName(newName);
        return new ResponseEntity<>(toPharmacyDTO.convert(p), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @PutMapping(value = "/{id}/new-address", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyDTO> updatePharmacyAddress(@RequestBody AddressDTO newAddress, @PathVariable("id") Integer id) {
        Pharmacy p = pharmacyService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Address address = addressService.findAddress(newAddress.getName(), newAddress.getNumber(), newAddress.getCity(), newAddress.getPostalCode());
        if (address == null){
            address = new Address(newAddress.getName(), newAddress.getCity(), newAddress.getNumber(),
                    newAddress.getPostalCode(), newAddress.getLatitude(), newAddress.getLongitude());
            address = addressService.save(address);
        }
        pharmacyService.updateAddress(id, address.getId());
        p.setAddress(address);
        return new ResponseEntity<>(toPharmacyDTO.convert(p), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/dermatologists/{derid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> dermatologistDismissal(@PathVariable("id") Integer id, @PathVariable("derid") Integer dermatologistId) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(pharmacy.dermatologistDismissal(dermatologistId), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}/pharmacists/{pharid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> pharmacistDismissal(@PathVariable("id") Integer id, @PathVariable("pharid") Integer pharmacistId) {
        Pharmacy pharmacy = pharmacyService.findOne(id);
        if(pharmacy == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(pharmacy.pharmacistDismissal(pharmacistId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Pharmacy> deletePharmacy(@PathVariable("id") Integer id) {
        // TODO: Logicko brisanje?

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
