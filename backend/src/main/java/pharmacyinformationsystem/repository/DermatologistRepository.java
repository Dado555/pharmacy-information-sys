package pharmacyinformationsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.users.Dermatologist;

import java.util.List;

@Repository
public interface DermatologistRepository extends JpaRepository<Dermatologist, Integer> {

    @Query("select dermatologist from Dermatologist dermatologist where dermatologist.active = true")
    List<Dermatologist> findAll();

    @Query(value = "select dermatologist from Dermatologist dermatologist where dermatologist.active = true")
    Page<Dermatologist> findAll(Pageable pageable);

    Dermatologist findByIdAndActiveTrue(Integer id);

    @Query("select dermatologist from Dermatologist dermatologist where dermatologist.active = true and lower(dermatologist.firstName) like lower(concat('%', ?1, '%')) " +
            "and lower(dermatologist.lastName) like lower(concat('%', ?2, '%'))" +
            "and lower(dermatologist.phoneNumber) like lower(concat('%', ?3, '%'))" +
            "and (lower(dermatologist.address.city) like lower(concat('%', ?4, '%'))" +
            "or lower(dermatologist.address.postalCode) like lower(concat('%', ?4, '%'))" +
            "or lower(dermatologist.address.name) like lower(concat('%', ?4, '%'))" +
            "or lower(dermatologist.address.number) like lower(concat('%', ?4, '%')))" +
            "and (?5 = 0 or " +
            "(select count(pharmacies.pharmacy.id) from dermatologist.dermacy pharmacies where pharmacies.pharmacy.id in ?6) >= 1)")
    Page<Dermatologist> findAllBySearchCriteria(String firstName, String lastName, String phoneNumber, String address,
                                                Integer size, List<Integer> pharmacyIds, Pageable pageable);

    @Query("select derm from Dermatologist derm where derm.active = true and derm.id not in (select dermacy.dermatologist.id from Dermacy dermacy where dermacy.pharmacy.id = ?1)")
    List<Dermatologist> findEmployable(Integer pharmacyId);

    Dermatologist findDermatologistByEmail(String email);
}
