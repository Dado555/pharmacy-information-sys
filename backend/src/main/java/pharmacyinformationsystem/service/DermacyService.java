package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Dermacy;

import java.util.List;

public interface DermacyService extends GenericService<Dermacy> {
    void dermatologistDismissal(Integer dermatologistId, Integer pharmacyId);

    Dermacy findOneByDermatologistIdAndPharmacyId(Integer dermatologistId, Integer pharmacyId);

    List<Dermacy> dermacies(Integer dermatologistId);

    void createNew(Integer pharmacyId, Integer dermatologistId);
}
