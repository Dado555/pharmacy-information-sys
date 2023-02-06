package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.PharmacyMedicine;
import pharmacyinformationsystem.model.Subscription;
import pharmacyinformationsystem.web.dto.PharmacyMedicineEdit;
import pharmacyinformationsystem.web.dto.domain.PharmacyMedicineDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicineSearchParameters;

import java.util.List;
import java.util.Map;

public interface PharmacyMedicineService extends GenericService<PharmacyMedicine>, AdvancedSearchService<PharmacyMedicine, MedicineSearchParameters> {
    void deleteByMedicineIdAndPharmacyId(Integer medicineId, Integer pharmacyId);

    void updateMedicine(Integer id, Double price, Integer availableAmount);

    PharmacyMedicine findPharmacyMedicine(Integer pharmacyId, Integer medicineId);

    List<PharmacyMedicine> findReplacementMedicines(Integer id);

    void setActionPrice(Integer id, Double actionPrice, Integer until);

    List<PharmacyMedicine> findPharmaciesForMedicine(Integer id);

    List<PharmacyMedicine> findPharmaciesForMedicineAndAmount(Integer medicineId, Integer amount);

    Integer prescribeMedicine(Map<String, Integer> patientAmount, Integer id);

    List<Integer> medicinesSoldMonthly(Integer id);

    List<Integer> medicinesSoldYearly(Integer id);

    void setActionPrice(Integer pharmacyId, PharmacyMedicineDTO medicineEdit);

    void updatePharmacyMedicine(PharmacyMedicineEdit medicineEdit);

    void sendNewPromotionMail(String title, String content, List<javax.mail.Address> emailRej);

    void promotionMail(PharmacyMedicineDTO medicineEdit, PharmacyMedicine pharmed,
                              List<Subscription> subscriptions, String pharmacyName);

    Integer removeMedicine(Map<String, Integer> patientAmount, Integer id);
}
