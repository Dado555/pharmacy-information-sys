package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.users.Penalty;
import pharmacyinformationsystem.web.dto.users.PenaltyDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PenaltyToPenaltyDTO implements Converter<Penalty, PenaltyDTO> {

    @Override
    public PenaltyDTO convert(Penalty penalty) {
        return new PenaltyDTO(penalty.getDate(), penalty.getDescription());
    }

    public List<PenaltyDTO> convert(List<Penalty> penalties) {
        return penalties.stream().map(this::convert).collect(Collectors.toList());
    }
}
