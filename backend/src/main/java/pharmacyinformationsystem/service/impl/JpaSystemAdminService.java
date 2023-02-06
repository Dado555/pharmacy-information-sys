package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Complaint;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.repository.SystemAdminRepository;
import pharmacyinformationsystem.service.AddressService;
import pharmacyinformationsystem.service.RoleService;
import pharmacyinformationsystem.service.SystemAdminService;

import javax.persistence.LockModeType;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class JpaSystemAdminService implements SystemAdminService {
    private final SystemAdminRepository systemAdminRepository;
    private final AddressService addressService;
    private final RoleService roleService;

    @Autowired
    public JpaSystemAdminService(SystemAdminRepository systemAdminRepository, AddressService addressService,
                                 RoleService roleService) {
        this.systemAdminRepository = systemAdminRepository;
        this.addressService = addressService;
        this.roleService = roleService;
    }
    @Override
    public List<SystemAdmin> findAll() {
        return systemAdminRepository.findAll();
    }

    @Override
    public SystemAdmin findOne(Integer id) {
        return systemAdminRepository.findById(id).orElse(null);
    }

    @Override
    public SystemAdmin save(SystemAdmin entity) {
        return systemAdminRepository.save(entity);
    }

    @Override
    public List<SystemAdmin> findSystemAdminsByFirstAndLastName(String firstAndLastName) {
        return systemAdminRepository.findSystemAdminsByFirstAndLastName(firstAndLastName);
    }

    @Override
    public SystemAdmin findSystemAdminByEmail(String email) {
        return systemAdminRepository.findSystemAdminByEmail(email);
    }

    @Override
    public SystemAdmin createSystemAdmin(SystemAdmin systemAdmin) {
        if (findSystemAdminByEmail(systemAdmin.getEmail()) != null) {
            return null;
        }

        Address address = addressService.save(systemAdmin.getAddress());
        systemAdmin.setAddress(address);
        systemAdmin.setActive(true);
        Role role = roleService.findByName("ROLE_SYSTEM_ADMIN");
        systemAdmin.setRole(role);
        return save(systemAdmin);
    }

    @Override
    public Page<SystemAdmin> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return systemAdminRepository.findAll(pageable);
    }

    @Override
    public List<Complaint> systemAdminComplaints(Integer id) {
        SystemAdmin systemAdmin = findOne(id);
        if (systemAdmin == null)
            return null;
        return new ArrayList<>(systemAdmin.getComplaints());
    }
}
