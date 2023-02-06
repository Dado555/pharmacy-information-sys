package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Subscription;
import pharmacyinformationsystem.repository.SubscriptionRepository;
import pharmacyinformationsystem.service.SubscriptionService;

import java.util.List;

@Service
public class JpaSubscriptionService implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public JpaSubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription findOne(Integer id) {
        return subscriptionRepository.findSubscriptionById(id);
    }

    @Override
    public Subscription save(Subscription entity) {
        return subscriptionRepository.save(entity);
    }

    @Override
    public Subscription findSubscription(Integer patientId, Integer pharmacyId) {
        return subscriptionRepository.findSubscriptionByPatientIdAndPharmacyId(patientId, pharmacyId);
    }

    @Override
    public void deleteSubscription(Integer patientId, Integer pharmacyId) {
        subscriptionRepository.deleteSubscriptionByPatientIdAndPharmacyId(patientId, pharmacyId);
    }

    @Override
    public List<Subscription> findSubs(Integer pharmacyId) {
        return subscriptionRepository.findSubscriptionsByPharmacyId(pharmacyId);
    }

    @Override
    public List<Subscription> findSubsByPatientId(Integer patientId) {
        return subscriptionRepository.findAllByPatientId(patientId);
    }
}
