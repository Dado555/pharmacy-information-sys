package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.users.PharmacyAdmin;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.repository.PharmacyAdminRepository;
import pharmacyinformationsystem.service.*;

import java.util.List;

@Service
public class JpaPharmacyAdminService implements PharmacyAdminService {
    private final PharmacyAdminRepository pharmacyAdminRepository;

    private final RoleService roleService;
    private final PharmacyService pharmacyService;
    private final AddressService addressService;

    @Autowired
    public JpaPharmacyAdminService(PharmacyAdminRepository pharmacyAdminRepository, RoleService roleService,
                                   PharmacyService pharmacyService, AddressService addressService) {
        this.pharmacyAdminRepository = pharmacyAdminRepository;
        this.roleService = roleService;
        this.pharmacyService = pharmacyService;
        this.addressService = addressService;
    }
    @Override
    public List<PharmacyAdmin> findAll() {
        return pharmacyAdminRepository.findAll();
    }

    @Override
    public PharmacyAdmin findOne(Integer id) {
        return pharmacyAdminRepository.findById(id).orElse(null);
    }

    @Override
    public PharmacyAdmin save(PharmacyAdmin entity) {
        return pharmacyAdminRepository.save(entity);
    }

    @Override
    public List<PharmacyAdmin> findPharmacyAdminsByFirstAndLastName(String firstAndLastName) {
        return pharmacyAdminRepository.findPharmacyAdminsByFirstAndLastName(firstAndLastName);
    }

    @Override
    public PharmacyAdmin findPharmacyAdminByEmail(String email) {
        return pharmacyAdminRepository.findPharmacyAdminByEmail(email);
    }

    @Override
    public PharmacyAdmin createPharmacyAdmin(PharmacyAdmin pharmacyAdmin, Integer pharmacyId) {
        if (findPharmacyAdminByEmail(pharmacyAdmin.getEmail()) != null) {
            return null;
        }

        if (pharmacyService.findOne(pharmacyAdmin.getPharmacy().getId()) == null) {
            return null;
        }

        Address address = addressService.save(pharmacyAdmin.getAddress());
        pharmacyAdmin.setAddress(address);
        Pharmacy pharmacy = pharmacyService.findOne(pharmacyId);
        pharmacyAdmin.setPharmacy(pharmacy);
        pharmacyAdmin.setActive(true);
        Role role = roleService.findByName("ROLE_PHARMACY_ADMIN");
        pharmacyAdmin.setRole(role);
        return save(pharmacyAdmin);
    }

    @Override
    public Page<PharmacyAdmin> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return pharmacyAdminRepository.findAll(pageable);
    }
}
