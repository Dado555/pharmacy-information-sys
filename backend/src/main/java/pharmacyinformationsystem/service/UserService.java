package pharmacyinformationsystem.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.model.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService extends GenericService<User>, UserDetailsService {

    User findByEmail(String email);
    List<User> findAllByRole(UserRole role);
    Optional<User> findById(Integer id);

    User getAuthenticatedUser();

    void firstLogin(SystemAdmin user, String email, String password);
}
