package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.MedicineReservation;
import pharmacyinformationsystem.model.MedicineReservationItem;
import pharmacyinformationsystem.service.PharmacyMedicineService;
import pharmacyinformationsystem.web.dto.domain.MedicineReservationDTO;
import pharmacyinformationsystem.web.dto.domain.MedicineReservationItemDTO;

@Component
public class MedicineReservationDTOToMedicineReservation implements Converter<MedicineReservationDTO, MedicineReservation> {

    private final PharmacyMedicineService pharmacyMedicineService;

    @Autowired
    public MedicineReservationDTOToMedicineReservation(PharmacyMedicineService pharmacyMedicineService) {
        this.pharmacyMedicineService = pharmacyMedicineService;
    }

    @Override
    public MedicineReservation convert(MedicineReservationDTO medicineReservationDTO) {
        MedicineReservation reservation = new MedicineReservation((medicineReservationDTO.getDateAndTime()));
        for (MedicineReservationItemDTO item: medicineReservationDTO.getMedicineReservationItemDTOList())
            reservation.addReservationItem(new MedicineReservationItem(pharmacyMedicineService.findOne(item.getPharmacyMedicineDTO().getId()), item.getAmount()));

        return reservation;
    }
}
