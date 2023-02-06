package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.QRCodeResult;
import pharmacyinformationsystem.web.dto.domain.EPrescriptionDTO;
import pharmacyinformationsystem.web.dto.domain.PharmacyDTO;
import pharmacyinformationsystem.web.dto.domain.QRCodeResultDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QRCodeResultToQRCodeResultDTO implements Converter<QRCodeResult, QRCodeResultDTO> {
    private final PharmacytoPharmacyDTO pharmacytoPharmacyDTO;
    private final EPrescriptionToEPrescriptionDTO ePrescriptionToEPrescriptionDTO;

    public QRCodeResultToQRCodeResultDTO(PharmacytoPharmacyDTO pharmacytoPharmacyDTO, EPrescriptionToEPrescriptionDTO ePrescriptionToEPrescriptionDTO) {
        this.pharmacytoPharmacyDTO = pharmacytoPharmacyDTO;
        this.ePrescriptionToEPrescriptionDTO = ePrescriptionToEPrescriptionDTO;
    }

    @Override
    public QRCodeResultDTO convert(QRCodeResult result) {
        PharmacyDTO pharmacyDTO = pharmacytoPharmacyDTO.convert(result.getPharmacy());
        EPrescriptionDTO ePrescriptionDTO = ePrescriptionToEPrescriptionDTO.convert(result.getEPrescription());
        return new QRCodeResultDTO(result.getId(), ePrescriptionDTO, pharmacyDTO, result.getTotalPrice());
    }

    public List<QRCodeResultDTO> convert(List<QRCodeResult> list) {
        return list.stream().map(this::convert).collect(Collectors.toList());
    }
}
