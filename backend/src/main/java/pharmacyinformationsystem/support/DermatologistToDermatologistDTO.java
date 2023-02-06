package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.service.RatingService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.web.dto.users.DermatologistDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DermatologistToDermatologistDTO implements Converter<Dermatologist, DermatologistDTO> {

    private final RatingService ratingService;
    private final UserService userService;
    private final AddressToAddressDTO addressToAddressDTO;

    @Autowired
    public DermatologistToDermatologistDTO(RatingService ratingService, UserService userService, AddressToAddressDTO addressToAddressDTO) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.addressToAddressDTO = addressToAddressDTO;
    }

    @Override
    public DermatologistDTO convert(Dermatologist dermatologist) {
        boolean rateable;
        try { rateable = ratingService.isDermatologistRateable(dermatologist.getId(), userService.getAuthenticatedUser().getId()); }
        catch(Exception e) { rateable = false; }

        Rating rating = ratingService.findRatingByPatientAndMedicalWorker(userService.getAuthenticatedUser().getId(), dermatologist.getId());
        Integer patientRating = (rating != null) ? rating.getValue() : null;

        return new DermatologistDTO(dermatologist.getId(),
                dermatologist.getEmail(),
                dermatologist.getFirstName(),
                dermatologist.getLastName(),
                dermatologist.getPhoneNumber(),
                addressToAddressDTO.convert(dermatologist.getAddress()),
                dermatologist.getRole().getName(),
                dermatologist.getActive(),
                dermatologist.getRating(),
                dermatologist.getDermacy() == null ? null : dermatologist.getDermacy().stream()
                        .map(entity -> entity.getPharmacy().getId()).collect(Collectors.toList()),
                rateable, patientRating);
    }

    public List<DermatologistDTO> convert(List<Dermatologist> dermatologist) {
        return dermatologist.stream().map(this::convert).collect(Collectors.toList());
    }

    private Double calculateRating(List<MedicalWorkerRating> ratings) {
        double rating = 0.0;
        for(MedicalWorkerRating mwr : ratings) {
            rating += mwr.getValue().doubleValue();
        }
        if(rating > 0) return rating/ratings.size();
        return rating;
    }
}
