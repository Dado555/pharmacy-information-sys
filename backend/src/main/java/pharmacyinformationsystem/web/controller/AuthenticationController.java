package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import pharmacyinformationsystem.model.users.Supplier;
import pharmacyinformationsystem.model.users.SystemAdmin;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.service.PatientService;
import pharmacyinformationsystem.service.SupplierService;
import pharmacyinformationsystem.service.SystemAdminService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.model.users.*;
import pharmacyinformationsystem.service.*;
import pharmacyinformationsystem.support.UserToUserDTO;
import pharmacyinformationsystem.web.dto.jwt.JwtAuthenticationRequestDTO;
import pharmacyinformationsystem.web.dto.users.UserDTO;
import pharmacyinformationsystem.web.dto.jwt.UserTokenStateDTO;
import pharmacyinformationsystem.web.util.TokenUtils;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final TokenUtils tokenUtils;
    public final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PatientService patientService;
    private final UserToUserDTO userToUserDTO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserService userService, PatientService patientService, UserToUserDTO userToUserDTO, PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.patientService = patientService;
        this.userService = userService;
        this.userToUserDTO = userToUserDTO;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserTokenStateDTO> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequestDTO authenticationRequest, HttpServletResponse response) {

        if (userService.findByEmail(authenticationRequest.getEmail()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String passwordEncoded = passwordEncoder.encode("null");
        if (userService.findByEmail(authenticationRequest.getEmail()).getPassword() == null) {
            userService.findByEmail(authenticationRequest.getEmail()).setPassword(passwordEncoded);
            authenticationRequest.setPassword("null");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();
        if (!user.getActive()) {
            jwt = "not_active";
        }
        if (user.getPassword().equals(passwordEncoded)) {
            jwt = "first_login";
        }
        return ResponseEntity.ok(new UserTokenStateDTO(jwt, expiresIn));
    }

    @GetMapping(value = "/authority", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDTO> getMyAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user = (User) authentication.getPrincipal();

        user = userService.findByEmail(user.getEmail());
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserDTO userDTO = userToUserDTO.convert(user);
        if (user.getRole().getName().equals("ROLE_PATIENT"))
            userDTO.setDiscount(patientService.findPatientByEmail(user.getEmail()).getPatientCategory().getDiscount());

        return new ResponseEntity<>(userDTO,  HttpStatus.OK);
    }

    @PutMapping(value = "/login/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RedirectView changePassword(@RequestBody SystemAdmin user, @PathVariable String email) {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("http://pis-front.herokuapp.com/login");
        userService.firstLogin(user, email, passwordEncoder.encode(user.getPassword()));
        return redirect;
    }

}
