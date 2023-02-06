package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Dermacy;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.repository.DermacyRepository;
import pharmacyinformationsystem.repository.DermatologistRepository;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.service.DermacyService;

import java.util.List;

@Service
@Transactional
public class JpaDermacyService implements DermacyService {

    private final DermacyRepository dermacyRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DermatologistRepository dermatologistRepository;

    @Autowired
    public JpaDermacyService(DermacyRepository dermacyRepository1, PharmacyRepository pharmacyRepository, DermatologistRepository dermatologistRepository) {
        this.dermacyRepository = dermacyRepository1;
        this.pharmacyRepository = pharmacyRepository;
        this.dermatologistRepository = dermatologistRepository;
    }

    @Override
    public void dermatologistDismissal(Integer dermatologistId, Integer pharmacyId) {
        dermacyRepository.deleteDermacyByDermatologistIdAndPharmacyId(dermatologistId, pharmacyId);
    }

    @Override
    public Dermacy findOneByDermatologistIdAndPharmacyId(Integer dermatologistId, Integer pharmacyId) {
        return dermacyRepository.findDermacyByDermatologistIdAndPharmacyId(dermatologistId, pharmacyId);
    }

    @Override
    public List<Dermacy> dermacies(Integer dermatologistId) {
        return dermacyRepository.findAllByDermatologistId(dermatologistId);
    }

    @Override
    public void createNew(Integer pharmacyId, Integer dermatologistId) {
        Pharmacy pharmacy = pharmacyRepository.getOne(pharmacyId);
        Dermatologist dermatologist = dermatologistRepository.getOne(dermatologistId);
        Dermacy dermacy = new Dermacy(dermatologist, pharmacy);
        dermacyRepository.save(dermacy);
    }

    @Override
    public List<Dermacy> findAll() {
        return dermacyRepository.findAll();
    }

    @Override
    public Dermacy findOne(Integer id) {
        return dermacyRepository.findDermacyById(id);
    }

    @Override
    public Dermacy save(Dermacy entity) {
        return dermacyRepository.save(entity);
    }
}
