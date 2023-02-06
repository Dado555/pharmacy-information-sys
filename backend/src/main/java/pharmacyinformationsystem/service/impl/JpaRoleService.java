package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.repository.RoleRepository;
import pharmacyinformationsystem.service.RoleService;

@Service
@Transactional(readOnly = true)
public class JpaRoleService implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public JpaRoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(Integer id) {
        return this.roleRepository.getOne(id);
    }

    @Override
    public Role findByName(String name) {
        return this.roleRepository.findByName(name);
    }

}
