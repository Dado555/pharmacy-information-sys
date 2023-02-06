package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.MedicineInquiry;
import pharmacyinformationsystem.repository.MedicineInquiryRepository;
import pharmacyinformationsystem.service.MedicineInquiryService;

import java.util.List;

@Service
@Transactional
public class JpaMedicineInquiryService implements MedicineInquiryService {
    private final MedicineInquiryRepository medicineInquiryRepository;

    @Autowired
    public JpaMedicineInquiryService(MedicineInquiryRepository medicineInquiryRepository) {
        this.medicineInquiryRepository = medicineInquiryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineInquiry> findAll() {
        return medicineInquiryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineInquiry findOne(Integer id) {
        return medicineInquiryRepository.findById(id).orElse(null);
    }

    @Override
    public MedicineInquiry save(MedicineInquiry entity) {
        return medicineInquiryRepository.save(entity);
    }
}
