package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.users.Role;

public interface RoleService {
    Role findById(Integer id);
    Role findByName(String name);
}
