package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.web.dto.domain.AddressDTO;


@Component
public class AddressToAddressDTO implements Converter<Address, AddressDTO> {

    @Override
    public AddressDTO convert(Address address) {
        return new AddressDTO(address.getId(), address.getName(), address.getCity(), address.getNumber(), address.getPostalCode(), address.getLongitude(), address.getLatitude());
    }

}
