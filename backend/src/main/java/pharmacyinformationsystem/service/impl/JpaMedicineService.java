package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.repository.MedicineRepository;
import pharmacyinformationsystem.service.MedicineService;
import pharmacyinformationsystem.service.base.MedicineServiceBase;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.*;

@Service
@Transactional
public class JpaMedicineService extends MedicineServiceBase implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public JpaMedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medicine> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return medicineRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Medicine findOne(Integer id) {
        return medicineRepository.findByIdAndActiveTrue(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medicine> searchByCriteria(MedicineSearchParameters searchParameters, Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        if (!searchParameters.getSortBy().equals("") && searchParameters.getSortBy() != null) {
            Sort sort = Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(page.orElse(0), 8, sort);
        }

        if (!searchParameters.getFilterBy().equals("") && searchParameters.getFilterBy() != null)
            return medicineRepository.filterByType(searchParameters.getFilterBy(), pageable);

        return medicineRepository.findByNameAndTypeAndFilterByAverageRating(searchParameters.getName(), searchParameters.getType(), (double)searchParameters.getRatingFrom(), (double)searchParameters.getRatingTo(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicine> findUnregisteredForPharmacy(Integer pharmacyId) {
        return medicineRepository.findUnregisteredForPharmacy(pharmacyId);
    }

    @Override
    public Medicine deleteById(Integer id) {
        Medicine medicine = findOne(id);
        if (medicine == null)
            throw new NotFoundException("Medicine not found!");

        medicine.setActive(false);
        save(medicine);
        return medicine;
    }

    @Override
    @Transactional
    public Medicine save(Medicine entity) {
        if (entity == null)
            return null;
        if (entity.getRating() == null) {
            entity.setRating(0.0);
        }
        return medicineRepository.save(entity);
    }
}
