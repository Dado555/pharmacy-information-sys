package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.web.dto.users.PharmacyAdminDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PharmacyAdminToPharmacyAdminDTO implements Converter<PharmacyAdmin, PharmacyAdminDTO> {
    private final AddressToAddressDTO addressToAddressDTO;

    @Autowired
    public PharmacyAdminToPharmacyAdminDTO(AddressToAddressDTO addressToAddressDTO) {
        this.addressToAddressDTO = addressToAddressDTO;
    }

    @Override
    public PharmacyAdminDTO convert(PharmacyAdmin pharmacyAdmin) {
        return new PharmacyAdminDTO(pharmacyAdmin.getId(),
                pharmacyAdmin.getEmail(),
                pharmacyAdmin.getFirstName(),
                pharmacyAdmin.getLastName(),
                pharmacyAdmin.getPhoneNumber(),
                addressToAddressDTO.convert(pharmacyAdmin.getAddress()),
                pharmacyAdmin.getRole().getName(),
                pharmacyAdmin.getActive(),
                pharmacyAdmin.getPharmacy().getId());
    }

    public List<PharmacyAdminDTO> convert(List<PharmacyAdmin> pharmacyAdminList) {
        return pharmacyAdminList.stream().map(this::convert).collect(Collectors.toList());
    }
}
