package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.MedicineInquiry;
import pharmacyinformationsystem.web.dto.domain.MedicineInquiryDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicineInquiryToMedicineInquiryDTO implements Converter<MedicineInquiry, MedicineInquiryDTO>{
    @Override
    public MedicineInquiryDTO convert(MedicineInquiry inquiry) {
        return new MedicineInquiryDTO(inquiry.getId(), inquiry.getDateCreated(), inquiry.getResolved(),
                                      inquiry.getMedicine().getId(), inquiry.getPharmacy().getId());
    }

    public List<MedicineInquiryDTO> convert(List<MedicineInquiry> inquiries) {
        return inquiries.stream().map(this::convert).collect(Collectors.toList());
    }
}
