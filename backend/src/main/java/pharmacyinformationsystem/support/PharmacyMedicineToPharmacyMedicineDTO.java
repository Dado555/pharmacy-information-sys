package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PharmacyMedicineToPharmacyMedicineDTO implements Converter<PharmacyMedicine, PharmacyMedicineDTO> {

    private final PharmacytoPharmacyDTO pharmacytoPharmacyDTO;
    private final MedicineToMedicineDTO medicineToMedicineDTO;

    @Autowired
    public PharmacyMedicineToPharmacyMedicineDTO(PharmacytoPharmacyDTO pharmacytoPharmacyDTO, MedicineToMedicineDTO medicineToMedicineDTO) {
        this.pharmacytoPharmacyDTO = pharmacytoPharmacyDTO;
        this.medicineToMedicineDTO = medicineToMedicineDTO;
    }

    @Override
    public PharmacyMedicineDTO convert(PharmacyMedicine pharmacyMedicine) {
        return new PharmacyMedicineDTO(pharmacyMedicine.getId(),
                                        pharmacytoPharmacyDTO.convert(pharmacyMedicine.getPharmacy()),
                                        medicineToMedicineDTO.convert(pharmacyMedicine.getMedicine()),
                                        pharmacyMedicine.getPrice(),
                                        pharmacyMedicine.getAvailableAmount(),
                                        pharmacyMedicine.getActionPrice(),
                                        pharmacyMedicine.getUntilDateTime());
    }

    public List<PharmacyMedicineDTO> convert(List<PharmacyMedicine> pharmacyMedicineList) {
        return pharmacyMedicineList.stream().map(this::convert).collect(Collectors.toList());
    }
}
