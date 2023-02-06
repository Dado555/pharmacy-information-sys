package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.PharmacyMedicinePrice;
import pharmacyinformationsystem.model.PharmedActionPrice;
import pharmacyinformationsystem.repository.PharmedActionPriceRepository;
import pharmacyinformationsystem.repository.PharmedPriceRepository;
import pharmacyinformationsystem.service.PharmedPriceService;

import java.util.List;

@Service
public class JpaPharmedPriceService implements PharmedPriceService {
    private final PharmedPriceRepository pharmedPriceRepository;
    private final PharmedActionPriceRepository actionPriceRepository;

    @Autowired
    public JpaPharmedPriceService(PharmedPriceRepository pharmedPriceRepository, PharmedActionPriceRepository actionPriceRepository) {
        this.pharmedPriceRepository = pharmedPriceRepository;
        this.actionPriceRepository = actionPriceRepository;
    }

    @Override
    public List<PharmacyMedicinePrice> findAll() {
        return null;
    }

    @Override
    public PharmacyMedicinePrice findOne(Integer id) {
        return null;
    }

    @Override
    public PharmacyMedicinePrice save(PharmacyMedicinePrice entity) {
        return pharmedPriceRepository.save(entity);
    }

    @Override
    public PharmedActionPrice save(PharmedActionPrice entity) {
        return actionPriceRepository.save(entity);
    }
}
