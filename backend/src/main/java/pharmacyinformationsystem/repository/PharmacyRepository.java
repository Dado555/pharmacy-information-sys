package pharmacyinformationsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Pharmacy;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer> {

    @Query("select ph from Pharmacy ph where lower(ph.name) like lower(concat('%', ?1, '%')) and lower(ph.address.city) like lower(concat('%', ?2, '%'))")
    Page<Pharmacy> findByNameAndCity(String name, String city, Pageable pageable);

    Pharmacy findOneById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Modifying
    @Transactional
    @Query("update Pharmacy p set p.name = ?2 where p.id = ?1")
    void updateName(Integer id, String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Modifying
    @Transactional
    @Query("update Pharmacy p set p.address = (select address from Address address where address.id = ?2) where p.id = ?1")
    void updateAddress(Integer id, Integer addressId);
}
