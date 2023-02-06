package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.users.*;
import pharmacyinformationsystem.model.enums.UserRole;
import pharmacyinformationsystem.repository.UserRepository;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JpaUserService implements UserService {

    private final UserRepository userRepository;
    private final SystemAdminService systemAdminService;
    private final SupplierService supplierService;
    private final DermatologistService dermatologistService;
    private final PharmacistService pharmacistService;

    @Autowired
    public JpaUserService(UserRepository userRepository, SystemAdminService systemAdminService, SupplierService supplierService, DermatologistService dermatologistService, PharmacistService pharmacistService) {
        this.userRepository = userRepository;
        this.systemAdminService = systemAdminService;
        this.supplierService = supplierService;
        this.dermatologistService = dermatologistService;
        this.pharmacistService = pharmacistService;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return null;

        if (authentication == null)
            return null;

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (email == null)
            throw new NotFoundException("User does not exist!");

        User user = findByEmail(email);
        if (user == null)
            throw new NotFoundException("User not found!");

        return user;
    }

    @Override
    public void firstLogin(SystemAdmin user, String email, String password) {
        SystemAdmin newUser = systemAdminService.findSystemAdminByEmail(email);
        if (newUser != null) {
            newUser.setPassword(password);
            systemAdminService.save(newUser);
            return;
        }

        Supplier newUser2 = supplierService.findSupplierByEmail(email);
        if (newUser2 != null) {
            newUser2.setPassword(password);
            supplierService.save(newUser2);
            return;
        }

        Dermatologist newUser3 = dermatologistService.findDermatologistByEmail(email);
        if (newUser3 != null) {
            newUser3.setPassword(password);
            System.out.println(email);
            dermatologistService.save(newUser3);
            return;
        }

        Pharmacist newUser4 = pharmacistService.findPharmacistByEmail(email);
        if (newUser4 != null) {
            newUser4.setPassword(password);
            pharmacistService.save(newUser4);
            return;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findOne(Integer id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllByRole(UserRole role) {
        return this.userRepository.findAllByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id);
    }

    @Override
    public User save(User entity) {
        if (entity == null)
            return null;

        return this.userRepository.save(entity);
    }
}
