package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.web.dto.users.UserDTO;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserToUserDTO implements Converter<User, UserDTO> {

    private final AddressToAddressDTO addressToAddressDTO;

    @Autowired
    public UserToUserDTO(AddressToAddressDTO addressToAddressDTO) {
        this.addressToAddressDTO = addressToAddressDTO;
    }

    @Override
    public UserDTO convert(User user) {
        return new UserDTO(user.getId(),
                            user.getEmail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            addressToAddressDTO.convert(user.getAddress()),
                            user.getRole().getName(),
                            user.getActive());
    }

    public List<UserDTO> convert(List<User> userList) {
        return userList.stream().map(this::convert).collect(Collectors.toList());
    }
}
