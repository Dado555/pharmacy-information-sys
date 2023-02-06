package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import pharmacyinformationsystem.model.Dermacy;

import javax.transaction.Transactional;
import java.util.List;

public interface DermacyRepository extends JpaRepository<Dermacy, Integer> {
    Dermacy findDermacyById(Integer id);

    Dermacy findDermacyByDermatologistIdAndPharmacyId(Integer dermatologistId, Integer pharmacyId);

    @Modifying
    @Transactional
    void deleteDermacyByDermatologistIdAndPharmacyId(Integer dermatologistId, Integer pharmacyId);

    List<Dermacy> findAllByDermatologistId(Integer dermatologistId);
}
