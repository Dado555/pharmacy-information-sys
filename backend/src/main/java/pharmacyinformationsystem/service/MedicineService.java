package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;

import java.util.List;
import java.util.Optional;

public interface MedicineService extends GenericService<Medicine>, AdvancedSearchService<Medicine, MedicineSearchParameters> {

    Page<Medicine> findAll(Integer page);

    List<Medicine> findUnregisteredForPharmacy(Integer pharmacyId);

    Page<Medicine> searchByCriteria(MedicineSearchParameters searchParameters, Optional<Integer> page);

    Medicine deleteById(Integer id);
}
