package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.service.AddressService;
import pharmacyinformationsystem.service.ComplaintService;
import pharmacyinformationsystem.service.RoleService;
import pharmacyinformationsystem.service.SystemAdminService;
import pharmacyinformationsystem.support.ComplaintToComplaintDTO;
import pharmacyinformationsystem.support.SystemAdminToSystemAdminDTO;
import pharmacyinformationsystem.web.dto.domain.ComplaintDTO;
import pharmacyinformationsystem.web.dto.pages.PharmacyAdminPageDTO;
import pharmacyinformationsystem.web.dto.pages.SystemAdminPageDTO;
import pharmacyinformationsystem.web.dto.users.SystemAdminDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/systemAdmins")
public class SystemAdminController {
    private final SystemAdminService systemAdminService;
    private final AddressService addressService;
    private final SystemAdminToSystemAdminDTO systemAdminToSystemAdminDTO;
    private final RoleService roleService;
    private final ComplaintToComplaintDTO complaintToComplaintDTO;
    private final ComplaintService complaintService;

    @Autowired
    public SystemAdminController(SystemAdminService systemAdminService, AddressService addressService, SystemAdminToSystemAdminDTO systemAdminToSystemAdminDTO, RoleService roleService, ComplaintToComplaintDTO complaintToComplaintDTO, ComplaintService complaintService) {
        this.systemAdminService = systemAdminService;
        this.addressService = addressService;
        this.systemAdminToSystemAdminDTO = systemAdminToSystemAdminDTO;
        this.roleService = roleService;
        this.complaintToComplaintDTO = complaintToComplaintDTO;
        this.complaintService = complaintService;
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SystemAdminDTO>> getSystemAdmins() {
        List<SystemAdmin> systemAdmins = new ArrayList<>(systemAdminService.findAll());
        return new ResponseEntity<>(systemAdminToSystemAdminDTO.convert(systemAdmins), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemAdminDTO> getOneSystemAdmin(@PathVariable("id") Integer id) {
        SystemAdmin systemAdmin = systemAdminService.findOne(id);
        if (systemAdmin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(systemAdminToSystemAdminDTO.convert(systemAdmin), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemAdminDTO> createSystemAdmin(@RequestBody SystemAdmin systemAdmin) {
        return new ResponseEntity<>(systemAdminToSystemAdminDTO.convert(systemAdminService.createSystemAdmin(systemAdmin)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemAdminDTO> updateSystemAdmin(@RequestBody SystemAdmin systemAdmin, @PathVariable Integer id) {
        SystemAdmin p = systemAdminService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        p.update(systemAdmin.getFirstName(), systemAdmin.getLastName(), systemAdmin.getPhoneNumber());
        return new ResponseEntity<>(systemAdminToSystemAdminDTO.convert(systemAdminService.save(p)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SystemAdminDTO> deleteSystemAdmin(@PathVariable("id") Integer id) {
        SystemAdmin systemAdmin = systemAdminService.findOne(id);
        if (systemAdmin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        systemAdmin.setActive(false);
        return new ResponseEntity<>(systemAdminToSystemAdminDTO.convert(systemAdmin), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/{id}/complaints", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ComplaintDTO>> getComplaints(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(complaintToComplaintDTO.convert(complaintService.getComplaintsForSystemAdmin(id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/complaints", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        return new ResponseEntity<>(complaintToComplaintDTO.convert(complaintService.getComplaintsWithoutSystemAdmin()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PutMapping(value = "/update_complaint/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComplaintDTO> updateComplaint(@RequestBody Complaint complaint, @PathVariable Integer id) {
        return new ResponseEntity<>(complaintToComplaintDTO.convert(complaintService.updateComplaint(complaint, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SystemAdminPageDTO> getSystemAdminPage(@RequestParam Optional<Integer> page) {
        Page<SystemAdmin> systemAdmins = systemAdminService.findAll(page.orElse(0));
        List<SystemAdmin> systemAdminList = systemAdmins.toList();
        return new ResponseEntity<>(new SystemAdminPageDTO(
                systemAdminToSystemAdminDTO.convert(systemAdminList),
                systemAdmins.getTotalPages()) , HttpStatus.OK);
    }
}
