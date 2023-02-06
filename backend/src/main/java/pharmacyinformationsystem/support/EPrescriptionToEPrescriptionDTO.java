package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.EPrescription;
import pharmacyinformationsystem.service.EPrescriptionService;
import pharmacyinformationsystem.web.dto.domain.EPrescriptionDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EPrescriptionToEPrescriptionDTO  implements Converter<EPrescription, EPrescriptionDTO> {

    private final EPrescriptionService ePrescriptionService;
    private final EPrescriptionMedToEPrescriptionMedDTO ePrescriptionMedToEPrescriptionMedDTO;

    public EPrescriptionToEPrescriptionDTO(EPrescriptionService ePrescriptionService, EPrescriptionMedToEPrescriptionMedDTO ePrescriptionMedToEPrescriptionMedDTO) {
        this.ePrescriptionService = ePrescriptionService;
        this.ePrescriptionMedToEPrescriptionMedDTO = ePrescriptionMedToEPrescriptionMedDTO;
    }

    @Override
    public EPrescriptionDTO convert(EPrescription ePrescription) {
        return new EPrescriptionDTO(ePrescription.getId(), ePrescription.getDateOfIssue(), ePrescription.getStatus(),
                ePrescriptionMedToEPrescriptionMedDTO.convert(ePrescriptionService.getEPrescriptionMedicines(ePrescription.getId())));
    }

    public List<EPrescriptionDTO> convert(List<EPrescription> ePrescriptions) {
        return ePrescriptions.stream().map(this::convert).collect(Collectors.toList());
    }
}
