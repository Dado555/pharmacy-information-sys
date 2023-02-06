package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import pharmacyinformationsystem.model.Subscription;

import javax.transaction.Transactional;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Subscription findSubscriptionById(Integer id);

    Subscription findSubscriptionByPatientIdAndPharmacyId(Integer patientId, Integer pharmacyId);

    @Modifying
    @Transactional
    void deleteSubscriptionByPatientIdAndPharmacyId(Integer patientId, Integer pharmacyId);

    List<Subscription> findSubscriptionsByPharmacyId(Integer pharmacyId);

    List<Subscription> findAllByPatientId(Integer patientId);
}
