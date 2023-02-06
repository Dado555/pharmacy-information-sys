package pharmacyinformationsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter @Setter
public class PatientCategory extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "minimum_points", nullable = false, unique = true)
    private Integer minimumPoints;

    @Column(name = "discount", nullable = false, unique = true)
    private Double discount;

    @Column(name = "color", nullable = false, unique = true)
    private String color;

    public PatientCategory(String name, Integer minimumPoints, Double discount, String color) {
        this();
        this.setName(name);
        this.setMinimumPoints(minimumPoints);
        this.setDiscount(discount);
        this.setColor(color);
    }
}
