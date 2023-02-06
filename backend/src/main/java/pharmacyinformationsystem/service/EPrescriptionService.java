package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.EPrescription;
import pharmacyinformationsystem.model.EPrescriptionMedicine;
import pharmacyinformationsystem.web.dto.searchparameters.EPrescriptionSearchParameters;

import java.util.List;

public interface EPrescriptionService extends GenericService<EPrescription> {

    List<EPrescriptionMedicine> findMedicinesForEPrescription(Integer id);

    List<EPrescription> searchByCriteria(Integer patientId, EPrescriptionSearchParameters searchParameters);

    List<EPrescriptionMedicine> getEPrescriptionMedicines(Integer id);
}
