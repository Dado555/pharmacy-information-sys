package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pharmacyinformationsystem.model.users.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}