package pharmacyinformationsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Medicine;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {

    @Query("select m from Medicine m where m.active = true")
    List<Medicine> findAll();

    @Query(value = "select m from Medicine m where m.active = true")
    Page<Medicine> findAll(Pageable pageable);

    Medicine findByIdAndActiveTrue(Integer id);

    @Query("select m from Medicine m where m.active = true and lower(m.name) like lower(concat('%', ?1, '%')) and lower(m.type) like lower(concat('%', ?2, '%')) and m.rating >= ?3 and m.rating <= ?4")
    Page<Medicine> findByNameAndTypeAndFilterByAverageRating(String name, String type, Double ratingFrom, Double ratingTo, Pageable pageable);

    @Query("select m from Medicine m where m.active = true and m.id not in (select phmed.medicine.id from PharmacyMedicine phmed where phmed.pharmacy.id = ?1)")
    List<Medicine> findUnregisteredForPharmacy(Integer pharmacyId);

    @Query("select m from Medicine m where m.active = true and m.type = ?1")
    Page<Medicine> filterByType(String type, Pageable pageable);

}
