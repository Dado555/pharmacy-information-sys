package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.web.dto.domain.PharmacyDTO;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class PharmacyDTOToPharmacy implements Converter<PharmacyDTO, Pharmacy> {

    private final PharmacyService pharmacyService;

    @Autowired
    public PharmacyDTOToPharmacy(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @Override
    public Pharmacy convert(PharmacyDTO pharmacyDTO) {
        Pharmacy pharmacy;
        if (pharmacyDTO.getId() == null)
            pharmacy = new Pharmacy();
        else {
            pharmacy = pharmacyService.findOne(pharmacyDTO.getId());
            if (pharmacy == null) {
                pharmacy = new Pharmacy();
                pharmacy.setId(pharmacyDTO.getId());
            }
        }
        pharmacy.setName(pharmacyDTO.getName());
        pharmacy.setAddress(pharmacyService.findAddressById(pharmacyDTO.getAddress().getId()));
        return pharmacy;
    }

    public List<Pharmacy> convert(List<PharmacyDTO> pharmacyDTOList) {
        return pharmacyDTOList.stream().map(this::convert).collect(Collectors.toList());
    }
}
