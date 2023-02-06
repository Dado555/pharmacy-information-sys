package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findAddressByNameAndNumberAndCityAndPostalCode(String name, String number, String city, String postalCode);
}
