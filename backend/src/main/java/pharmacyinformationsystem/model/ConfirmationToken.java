package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column
    private String confirmationToken;

    @Column
    private Integer dataId;

    public ConfirmationToken(Integer dataId, String confirmationToken) {
        this.dataId = dataId;
        this.confirmationToken = confirmationToken;
    }
}
