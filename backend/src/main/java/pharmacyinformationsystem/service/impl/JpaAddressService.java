package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.repository.AddressRepository;
import pharmacyinformationsystem.service.AddressService;

import java.util.List;

@Service
public class JpaAddressService implements AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public JpaAddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    @Override
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findOne(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public Address save(Address entity) {
        if (entity.getLatitude() == null) {
            entity.setLatitude(0.0);
        }
        if (entity.getLongitude() == null) {
            entity.setLongitude(0.0);
        }
        return addressRepository.save(entity);
    }

    @Override
    public Address findAddress(String streetName, String number, String city, String postalCode) {
        return addressRepository.findAddressByNameAndNumberAndCityAndPostalCode(streetName, number, city, postalCode);
    }
}
