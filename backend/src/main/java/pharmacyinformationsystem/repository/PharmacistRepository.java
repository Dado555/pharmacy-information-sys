package pharmacyinformationsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.Pharmacist;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Integer> {

    @Query("select pharmacist from Pharmacist pharmacist where pharmacist.active = true")
    List<Pharmacist> findAll();

    @Query(value = "select pharmacist from Pharmacist pharmacist where pharmacist.active = true")
    Page<Pharmacist> findAll(Pageable pageable);

    Pharmacist findByIdAndActiveTrue(Integer id);

    List<Pharmacist> findAllByPharmacyIsNull();

    @Query("select pharmacist from Pharmacist pharmacist where pharmacist.active = true and lower(pharmacist.firstName) like lower(concat('%', ?1, '%')) " +
            "and lower(pharmacist.lastName) like lower(concat('%', ?2, '%'))" +
            "and lower(pharmacist.phoneNumber) like lower(concat('%', ?3, '%'))" +
            "and (lower(pharmacist.address.city) like lower(concat('%', ?4, '%'))" +
            "or lower(pharmacist.address.postalCode) like lower(concat('%', ?4, '%'))" +
            "or lower(pharmacist.address.name) like lower(concat('%', ?4, '%'))" +
            "or lower(pharmacist.address.number) like lower(concat('%', ?4, '%')))" +
            "and (?5 = 0 or (" +
            "select count(pharmacies.id) from pharmacist.pharmacy pharmacies where pharmacies.id in ?6) >= 1)")
    Page<Pharmacist> findAllBySearchCriteria(String firstName, String lastName, String phoneNumber, String address,
                                             Integer size, List<Integer> pharmacyId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Pharmacist p set p.pharmacy = null where p.id = ?1")
    void pharmacistDismissal(Integer id);

    @Transactional
    @Modifying
    @Query("update Pharmacist p set p.pharmacy = (select pharmacy from Pharmacy pharmacy where pharmacy.id = ?2) where p.id = ?1")
    void hirePharmacist(Integer id, Integer pharmacyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select pharmacist from Pharmacist pharmacist where pharmacist.id = :id and pharmacist.active = true")
    Pharmacist findOneByIdWithLock(@Param("id") Integer id);

    Pharmacist findPharmacistByEmail(String email);
}
