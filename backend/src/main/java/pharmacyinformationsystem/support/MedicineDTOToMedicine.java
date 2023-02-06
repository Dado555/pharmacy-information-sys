package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.service.MedicineService;
import pharmacyinformationsystem.web.dto.domain.MedicineDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicineDTOToMedicine implements Converter<MedicineDTO, Medicine> {

    private final MedicineService medicineService;

    @Autowired
    public MedicineDTOToMedicine(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @Override
    public Medicine convert(MedicineDTO medicineDTO) {
        Medicine medicine;

        if (medicineDTO.getId() == null)
            medicine = new Medicine();
        else {
            medicine = medicineService.findOne(medicineDTO.getId());
            if (medicine == null) {
                medicine = new Medicine();
                medicine.setId(medicineDTO.getId());
            }
        }
        medicine.setName(medicineDTO.getName());
        medicine.setType(medicineDTO.getType());
        medicine.setMedicineForm(medicineDTO.getMedicineForm());
        medicine.setStructure(medicineDTO.getStructure());
        medicine.setManufacture(medicineDTO.getManufacture());
        medicine.setMedicineIssuingType(medicineDTO.getMedicineIssuingType());
        medicine.setDailyIntake(medicineDTO.getDailyIntake());
        medicine.setContraindications(medicineDTO.getContraindications());

        return medicine;
    }

    public List<Medicine> convert(List<MedicineDTO> medicineDTOList) {
        return medicineDTOList.stream().map(this::convert).collect(Collectors.toList());
    }
}
