package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.EPrescriptionMedicine;
import pharmacyinformationsystem.repository.EPrescriptionMedicineRepository;
import pharmacyinformationsystem.service.EPrescriptionMedicineService;

import java.util.ArrayList;
import java.util.List;

@Service
public class JpaEPrescriptionMedicineService implements EPrescriptionMedicineService {
    private final EPrescriptionMedicineRepository ePrescriptionMedicineRepository;

    @Autowired
    public JpaEPrescriptionMedicineService(EPrescriptionMedicineRepository ePrescriptionMedicineRepository) {
        this.ePrescriptionMedicineRepository = ePrescriptionMedicineRepository;
    }
    @Override
    public List<EPrescriptionMedicine> findAll() {
        return new ArrayList<>();
    }

    @Override
    public EPrescriptionMedicine findOne(Integer id) {
        return null;
    }

    @Override
    public EPrescriptionMedicine save(EPrescriptionMedicine entity) {
        return ePrescriptionMedicineRepository.save(entity);
    }
}
