package pharmacyinformationsystem.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Subscription;
import pharmacyinformationsystem.web.dto.domain.SubscriptionDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionToSubscriptionDTO implements Converter<Subscription, SubscriptionDTO> {

    @Override
    public SubscriptionDTO convert(Subscription subscription) {
        return new SubscriptionDTO(subscription.getId(), subscription.getPatient().getId(),
                subscription.getPharmacy().getId(), subscription.getPharmacy().getName());
    }

    public List<SubscriptionDTO> convert(List<Subscription> subscriptions) {
        return subscriptions.stream().map(this::convert).collect(Collectors.toList());
    }

}
