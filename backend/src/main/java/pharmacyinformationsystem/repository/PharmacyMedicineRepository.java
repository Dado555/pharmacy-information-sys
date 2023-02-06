package pharmacyinformationsystem.repository;

import pharmacyinformationsystem.model.PharmacyMedicine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PharmacyMedicineRepository extends JpaRepository<PharmacyMedicine, Integer> {
    PharmacyMedicine findPharmacyMedicineByMedicineIdAndPharmacyId(Integer medicineId, Integer pharmacyId);

    @Query("select phamed from PharmacyMedicine phamed where lower(phamed.medicine.name) like lower(concat('%', ?1, '%')) " +
            "and lower(phamed.medicine.type) like lower(concat('%', ?2, '%')) and phamed.pharmacy.id = ?3 " +
            "and (phamed.rating >= ?4 and phamed.rating <= ?5)")
    Page<PharmacyMedicine> findByNameAndTypeAndPharmacyIdAndFilterByAverageRating(String name, String type, Integer pharmacyId, Double ratingFrom, Double ratingTo, Pageable pageable);

    @Modifying
    @Transactional
    void deletePharmacyMedicineByIdAndPharmacyId(Integer medicineId, Integer pharmacyId);

    @Modifying
    @Transactional
    @Query("update PharmacyMedicine pharmed set pharmed.price = ?2, pharmed.availableAmount = ?3 where pharmed.id = ?1")
    void updatePharmacyMedicine(Integer id, Double price, Integer available);

    @Modifying
    @Transactional
    @Query("update PharmacyMedicine pharmed set pharmed.actionPrice = ?2, pharmed.untilDateTime = ?3 where pharmed.id = ?1")
    void setActionPrice(Integer id, Double actionPrice, Integer until);

    @Modifying
    @Transactional
    @Query("select phamed from PharmacyMedicine phamed where phamed.medicine.id = ?1")
    List<PharmacyMedicine> findPharmacyMedicineByMedicine(Integer id);

    @Modifying
    @Transactional
    @Query("select phamed from PharmacyMedicine phamed where phamed.medicine.id = ?1 and phamed.availableAmount >= ?2")
    List<PharmacyMedicine> findPharmacyMedicinesByMedicineAndAvailableAmount(Integer medicineId, Integer amount);
}
