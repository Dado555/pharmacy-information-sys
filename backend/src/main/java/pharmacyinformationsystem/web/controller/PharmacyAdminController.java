package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.PharmacyAdminToPharmacyAdminDTO;
import pharmacyinformationsystem.web.dto.pages.DermatologistsPageDTO;
import pharmacyinformationsystem.web.dto.pages.PharmacyAdminPageDTO;
import pharmacyinformationsystem.web.dto.users.PharmacyAdminDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/pharmacyAdmins")
public class PharmacyAdminController {
    private final PharmacyAdminService pharmacyAdminService;
    private final AddressService addressService;
    private final PharmacyAdminToPharmacyAdminDTO pharmacyAdminToPharmacyAdminDTO;
    private final PharmacyService pharmacyService;
    private final RoleService roleService;

    @Autowired
    public PharmacyAdminController(PharmacyAdminService pharmacyAdminService, AddressService addressService, PharmacyAdminToPharmacyAdminDTO pharmacyAdminToPharmacyAdminDTO, PharmacyService pharmacyService, RoleService roleService) {
        this.pharmacyAdminService = pharmacyAdminService;
        this.addressService = addressService;
        this.pharmacyAdminToPharmacyAdminDTO = pharmacyAdminToPharmacyAdminDTO;
        this.pharmacyService = pharmacyService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PharmacyAdminDTO>> getPharmacyAdmins() {
        List<PharmacyAdmin> pharmacyAdmins = new ArrayList<PharmacyAdmin>(pharmacyAdminService.findAll());
        return new ResponseEntity<>(pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdmins), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PHARMACY_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyAdminDTO> getOnePharmacyAdmin(@PathVariable("id") Integer id) {
        PharmacyAdmin pharmacyAdmin = pharmacyAdminService.findOne(id);
        if (pharmacyAdmin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdmin), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyAdminDTO> createPharmacyAdmin(@RequestBody PharmacyAdmin pharmacyAdmin, @PathVariable Integer id) {
        return new ResponseEntity<>(pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdminService.createPharmacyAdmin(pharmacyAdmin, id)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyAdminPageDTO> getPharmacyAdminPage(@RequestParam Optional<Integer> page) {
        Page<PharmacyAdmin> pharmacyAdmins = pharmacyAdminService.findAll(page.orElse(0));
        List<PharmacyAdmin> pharmacyAdminList = pharmacyAdmins.toList();
        return new ResponseEntity<>(new PharmacyAdminPageDTO(
                pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdminList),
                pharmacyAdmins.getTotalPages()) , HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'PHARMACY_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PharmacyAdminDTO> updatePharmacyAdmin(@RequestBody PharmacyAdmin pharmacyAdmin, @PathVariable Integer id) {
        PharmacyAdmin p = pharmacyAdminService.findOne(id);
        if (p == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        p.update(pharmacyAdmin.getFirstName(), pharmacyAdmin.getLastName(), pharmacyAdmin.getPhoneNumber());
        return new ResponseEntity<>(pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdminService.save(p)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PharmacyAdminDTO> deletePharmacyAdmin(@PathVariable("id") Integer id) {
        PharmacyAdmin pharmacyAdmin = pharmacyAdminService.findOne(id);
        if (pharmacyAdmin == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        pharmacyAdmin.setActive(false);
        return new ResponseEntity<>(pharmacyAdminToPharmacyAdminDTO.convert(pharmacyAdmin), HttpStatus.OK);
    }
}
