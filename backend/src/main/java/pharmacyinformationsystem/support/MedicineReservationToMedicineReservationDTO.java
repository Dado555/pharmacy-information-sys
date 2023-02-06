package pharmacyinformationsystem.support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.MedicineReservationItem;
import pharmacyinformationsystem.service.MedicineReservationService;
import pharmacyinformationsystem.web.dto.domain.MedicineReservationDTO;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicineReservationToMedicineReservationDTO implements Converter<MedicineReservation, MedicineReservationDTO> {

    private final MedicineReservationService medicineReservationService;
    private final MedicineReservationItemToMedicineReservationItemDTO medicineReservationItemToMedicineReservationItemDTO;

    @Autowired
    public MedicineReservationToMedicineReservationDTO(MedicineReservationService medicineReservationService, MedicineReservationItemToMedicineReservationItemDTO medicineReservationItemToMedicineReservationItemDTO) {
        this.medicineReservationService = medicineReservationService;
        this.medicineReservationItemToMedicineReservationItemDTO = medicineReservationItemToMedicineReservationItemDTO;
    }

    @Override
    public MedicineReservationDTO convert(MedicineReservation medicineReservation) {

        List<MedicineReservationItem> items = medicineReservationService.getMedicineReservationItems(medicineReservation.getId());
        return new MedicineReservationDTO(medicineReservation.getId(),
                                            medicineReservationItemToMedicineReservationItemDTO.convert(items),
                                            medicineReservation.getDateAndTime(),
                                            items.stream().mapToDouble(item -> item.getPharmacyMedicine().getPrice() - item.getDiscount()).sum(), medicineReservation.getStatus());
    }

    public List<MedicineReservationDTO> convert(List<MedicineReservation> reservationList) {
        return reservationList.stream().map(this::convert).collect(Collectors.toList());
    }
}
