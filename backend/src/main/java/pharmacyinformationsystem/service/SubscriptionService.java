package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Subscription;

import java.util.List;

public interface SubscriptionService extends GenericService<Subscription>{
    Subscription findSubscription(Integer patientId, Integer pharmacyId);
    void deleteSubscription(Integer patientId, Integer pharmacyId);
    List<Subscription> findSubs(Integer pharmacyId);
    List<Subscription> findSubsByPatientId(Integer patientId);
}
