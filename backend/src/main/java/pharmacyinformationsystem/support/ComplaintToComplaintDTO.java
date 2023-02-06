package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.Patient;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.web.dto.domain.ComplaintDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplaintToComplaintDTO implements Converter<Complaint, ComplaintDTO> {

    @Override
    public ComplaintDTO convert(Complaint complaint) {
        ComplaintDTO complaintDTO = new ComplaintDTO(complaint.getId(), complaint.getComplaintMessage(),
                complaint.getComplaintResponse(), 0, null, 0, complaint.getEntity());
        Patient patient = complaint.getPatient();
        if (patient != null) {
            complaintDTO.setPatientId(patient.getId());
            complaintDTO.setPatientEmail(patient.getEmail());
        }
        SystemAdmin systemAdmin = complaint.getSystemAdmin();
        if (systemAdmin != null) {
            complaintDTO.setSystemAdminId(systemAdmin.getId());
        }
        return complaintDTO;
    }

    public List<ComplaintDTO> convert(List<Complaint> complaintList) {
        return complaintList.stream().map(this::convert).collect(Collectors.toList());
    }
}
