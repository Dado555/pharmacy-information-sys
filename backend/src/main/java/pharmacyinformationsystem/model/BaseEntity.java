package pharmacyinformationsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@MappedSuperclass
@AllArgsConstructor @Getter @Setter
@Where(clause = "active = true")
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public BaseEntity() {
        this.active = true;
    }
}
