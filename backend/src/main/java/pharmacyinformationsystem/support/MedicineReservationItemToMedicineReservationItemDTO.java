package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.MedicineReservationItem;
import pharmacyinformationsystem.web.dto.domain.MedicineReservationItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicineReservationItemToMedicineReservationItemDTO implements Converter<MedicineReservationItem, MedicineReservationItemDTO> {

    private final PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO;

    @Autowired
    public MedicineReservationItemToMedicineReservationItemDTO(PharmacyMedicineToPharmacyMedicineDTO pharmacyMedicineToPharmacyMedicineDTO) {
        this.pharmacyMedicineToPharmacyMedicineDTO = pharmacyMedicineToPharmacyMedicineDTO;
    }

    @Override
    public MedicineReservationItemDTO convert(MedicineReservationItem medicineReservationItem) {
        Double price;
        if (medicineReservationItem.getPharmacyMedicine().getActionPrice() != null) {
            price = medicineReservationItem.getAmount() * medicineReservationItem.getPharmacyMedicine().getActionPrice();
        } else {
            price = medicineReservationItem.getAmount() * medicineReservationItem.getPharmacyMedicine().getPrice();
        }
        return new MedicineReservationItemDTO(medicineReservationItem.getId(),
                                                pharmacyMedicineToPharmacyMedicineDTO.convert(medicineReservationItem.getPharmacyMedicine()),
                                                medicineReservationItem.getAmount(), medicineReservationItem.getMedicineReservation().getId(), price, medicineReservationItem.getMedicineReservation().getDateAndTime(),
                medicineReservationItem.getMedicineReservation().getPatient().getFirstName(), medicineReservationItem.getDiscount());
    }

    public List<MedicineReservationItemDTO> convert(List<MedicineReservationItem> medicineReservationItemList) {
        return medicineReservationItemList.stream().map(this::convert).collect(Collectors.toList());
    }
}
