package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.Address;

public interface AddressService extends GenericService<Address> {
    Address findAddress(String streetName, String number, String city, String postalCode);
}
