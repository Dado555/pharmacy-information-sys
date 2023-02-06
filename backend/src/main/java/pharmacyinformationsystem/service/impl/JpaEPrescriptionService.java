package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.EPrescription;
import pharmacyinformationsystem.model.EPrescriptionMedicine;
import pharmacyinformationsystem.model.enums.EPrescriptionStatus;
import pharmacyinformationsystem.repository.EPrescriptionRepository;
import pharmacyinformationsystem.service.EPrescriptionService;
import pharmacyinformationsystem.web.dto.searchparameters.EPrescriptionSearchParameters;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class JpaEPrescriptionService implements EPrescriptionService {

    private final EPrescriptionRepository repository;

    @Autowired
    public JpaEPrescriptionService(EPrescriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EPrescription> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public EPrescription findOne(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EPrescriptionMedicine> findMedicinesForEPrescription(Integer id) {
        EPrescription ePrescription = findOne(id);
        if (ePrescription == null)
            throw new NotFoundException("E-Prescription not found!");

        return new ArrayList<>(ePrescription.getEPrescriptionMedicineList());
    }

    @Override
    public EPrescription save(EPrescription entity) {
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<EPrescription> searchByCriteria(Integer patientId, EPrescriptionSearchParameters searchParameters) {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        if (!searchParameters.getSortBy().equals("") && searchParameters.getSortBy() != null) {
            Sort sort = Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        }
        if (!searchParameters.getFilterBy().equals("") && searchParameters.getFilterBy() != null)
            return repository.filterByPatientAndStatus(patientId, EPrescriptionStatus.valueOf(searchParameters.getFilterBy()), pageable);

        return repository.filterByPatient(patientId, pageable);
    }

    public List<EPrescriptionMedicine> getEPrescriptionMedicines(Integer id) {
        EPrescription ePrescription = findOne(id);
        return new ArrayList<>(ePrescription.getEPrescriptionMedicineList());
    }
}
