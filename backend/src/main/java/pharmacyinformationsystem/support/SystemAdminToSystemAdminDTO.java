package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.service.SystemAdminService;
import pharmacyinformationsystem.web.dto.users.SystemAdminDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class SystemAdminToSystemAdminDTO implements Converter<SystemAdmin, SystemAdminDTO> {
    private final AddressToAddressDTO addressToAddressDTO;
    private final ComplaintToComplaintDTO complaintToComplaintDTO;
    private final SystemAdminService systemAdminService;

    @Autowired
    public SystemAdminToSystemAdminDTO(AddressToAddressDTO addressToAddressDTO, ComplaintToComplaintDTO complaintToComplaintDTO, SystemAdminService systemAdminService) {
        this.addressToAddressDTO = addressToAddressDTO;
        this.complaintToComplaintDTO = complaintToComplaintDTO;
        this.systemAdminService = systemAdminService;
    }

    @Override
    public SystemAdminDTO convert(SystemAdmin systemAdmin) {
        return new SystemAdminDTO(systemAdmin.getId(),
                systemAdmin.getEmail(),
                systemAdmin.getFirstName(),
                systemAdmin.getLastName(),
                systemAdmin.getPhoneNumber(),
                addressToAddressDTO.convert(systemAdmin.getAddress()),
                systemAdmin.getRole().getName(),
                systemAdmin.getActive());
    }

    public List<SystemAdminDTO> convert(List<SystemAdmin> systemAdminList) {
        List<SystemAdminDTO> systemAdminDTOList = new ArrayList<SystemAdminDTO>();
        for (SystemAdmin systemAdmin : systemAdminList) {
            SystemAdminDTO systemAdminDTO = this.convert(systemAdmin);
            for (Complaint complaint :  systemAdminService.systemAdminComplaints(systemAdmin.getId())) {
                systemAdminDTO.setComplaintDTO(complaintToComplaintDTO.convert(complaint));
            }
            systemAdminDTOList.add(systemAdminDTO);
        }
        return  systemAdminDTOList;
    }
}
