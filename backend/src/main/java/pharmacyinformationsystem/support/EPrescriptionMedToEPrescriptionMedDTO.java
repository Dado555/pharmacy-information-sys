package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.EPrescriptionMedicine;
import pharmacyinformationsystem.web.dto.domain.EPrescriptionMedicineDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EPrescriptionMedToEPrescriptionMedDTO implements Converter<EPrescriptionMedicine, EPrescriptionMedicineDTO> {

    @Override
    public EPrescriptionMedicineDTO convert(EPrescriptionMedicine ePrescriptionMedicine) {
        return new EPrescriptionMedicineDTO(ePrescriptionMedicine.getId(), ePrescriptionMedicine.getMedicine().getName(), ePrescriptionMedicine.getAmount(), ePrescriptionMedicine.getTherapyDuration());
    }

    public List<EPrescriptionMedicineDTO> convert(List<EPrescriptionMedicine> ePrescriptionMedicineList) {
        return ePrescriptionMedicineList.stream().map(this::convert).collect(Collectors.toList());
    }
}
