package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.repository.PenaltyRepository;
import pharmacyinformationsystem.web.dto.users.PatientDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientToPatientDTO implements Converter<Patient, PatientDTO> {

    private final AddressToAddressDTO addressToAddressDTO;
    private final PenaltyToPenaltyDTO penaltyToPenaltyDTO;
    private final PenaltyRepository penaltyRepository;

    @Autowired
    public PatientToPatientDTO(AddressToAddressDTO addressToAddressDTO, PenaltyToPenaltyDTO penaltyToPenaltyDTO, PenaltyRepository penaltyRepository) {
        this.addressToAddressDTO = addressToAddressDTO;
        this.penaltyToPenaltyDTO = penaltyToPenaltyDTO;
        this.penaltyRepository = penaltyRepository;
    }

    @Override
    public PatientDTO convert(Patient patient) {
        return new PatientDTO(patient.getId(),
                patient.getEmail(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                addressToAddressDTO.convert(patient.getAddress()),
                penaltyToPenaltyDTO.convert(penaltyRepository.findPenaltiesForPatient(patient.getId())),
                patient.getRole().getName(),
                patient.getActive(),
                patient.getPoints(),
                patient.getPatientCategory());
    }

    public List<PatientDTO> convert(List<Patient> patientList) {
        return patientList.stream().map(this::convert).collect(Collectors.toList());
    }
}
