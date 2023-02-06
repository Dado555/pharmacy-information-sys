package pharmacyinformationsystem.web.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter
@Setter
public class JwtAuthenticationRequestDTO {

    private String email;
    private String password;
}
