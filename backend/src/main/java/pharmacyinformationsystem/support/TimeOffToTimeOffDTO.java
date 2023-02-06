package pharmacyinformationsystem.support;

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import pharmacyinformationsystem.model.TimeOffRequest;
import pharmacyinformationsystem.web.dto.domain.TimeOffDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TimeOffToTimeOffDTO implements Converter<TimeOffRequest, TimeOffDTO> {

    @Override
    public TimeOffDTO convert(TimeOffRequest timeOffRequest) {
        return new TimeOffDTO(timeOffRequest.getId(), timeOffRequest.getContent(), timeOffRequest.getApproved(),
                              timeOffRequest.getStartDate(), timeOffRequest.getEndDate(),
                              timeOffRequest.getMedicalWorker().getId());
    }

    public List<TimeOffDTO> convert(List<TimeOffRequest> requests) {
        return requests.stream().map(this::convert).collect(Collectors.toList());
    }
}
