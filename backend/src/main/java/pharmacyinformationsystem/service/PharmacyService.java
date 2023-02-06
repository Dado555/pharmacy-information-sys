package pharmacyinformationsystem.service;

import org.springframework.data.domain.Page;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.web.dto.searchparameters.PharmacySearchParameters;

import java.util.List;
import java.util.Optional;


public interface PharmacyService extends GenericService<Pharmacy>, AdvancedSearchService<Pharmacy, PharmacySearchParameters> {

    Page<Pharmacy> findAll(Integer page);

    Address findAddressById(Integer id);

    Page<Pharmacy> searchByCriteria(PharmacySearchParameters searchParameters, Optional<Integer> page);

    List<Pharmacist> findPharmacistsByDateAndTimeCriteria(Integer id, Long start, Long end);

    List<Pharmacy> findAllByDateAndTimeCriteria(Long startDate, Long startTime, Long endTime);

    List<Pharmacy> filterByAverageRating(List<Pharmacy> pharmacies, Integer ratingFrom, Integer ratingTo);

    void updateName(Integer id, String name);

    void updateAddress(Integer id, Integer addressId);

    List<PharmacyMedicine> getPharmacyMedicines(Integer id);

    Double getAllEarnings(Integer id);

    List<Double> getMonthlyEarnings(Integer id);

    List<Double> getYearlyEarnings(Integer id);

    List<Dermatologist> getDermatologists(Integer id);

    List<Pharmacist> getPharmacists(Integer id);

    List<Order> getOrders(Integer id);

    List<TimeOffRequest> getTimeOffs(Integer id);

    List<MedicineInquiry> getMedicineInquiries(Integer id);

    List<Double> getMonthlyRatings(Integer id);

    List<Double> getYearlyRatings(Integer id);

    List<Subscription> getSubs(Integer id);

    Pharmacy savePharmacy(Pharmacy pharmacy);
}
