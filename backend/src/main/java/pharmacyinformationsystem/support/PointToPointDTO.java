package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;

import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Point;
import pharmacyinformationsystem.web.dto.domain.PointDTO;

@Component
public class PointToPointDTO implements Converter<Point, PointDTO> {

    @Override
    public PointDTO convert(Point point) {
        return new PointDTO(point.getId(), point.getPharmacistCounseling(), point.getDermatologistExamination());
    }
}
