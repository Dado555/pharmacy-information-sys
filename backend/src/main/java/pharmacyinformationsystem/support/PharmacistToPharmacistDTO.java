package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.service.RatingService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.web.dto.PharmacistDTO;

import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PharmacistToPharmacistDTO implements Converter<Pharmacist, PharmacistDTO> {

    private final RatingService ratingService;
    private final UserService userService;
    private final AddressToAddressDTO addressToAddressDTO;

    @Autowired
    public PharmacistToPharmacistDTO(RatingService ratingService, UserService userService, AddressToAddressDTO addressToAddressDTO) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.addressToAddressDTO = addressToAddressDTO;
    }

    @Override
    public PharmacistDTO convert(Pharmacist pharmacist) {
        boolean rateable;
        try { rateable = ratingService.isPharmacyRateable(pharmacist.getId(), userService.getAuthenticatedUser().getId()); }
        catch(Exception e) { rateable = false; }

        Rating rating = ratingService.findRatingByPatientAndMedicalWorker(userService.getAuthenticatedUser().getId(), pharmacist.getId());
        Integer patientRating = (rating != null) ? rating.getValue() : null;

        return new PharmacistDTO(pharmacist.getId(), pharmacist.getEmail(),
                                 pharmacist.getFirstName(), pharmacist.getLastName(),
                                 pharmacist.getPhoneNumber(), addressToAddressDTO.convert(pharmacist.getAddress()),
                                 pharmacist.getRole().getName(), pharmacist.getActive(),
                                 pharmacist.getRating(),
                                 pharmacist.getPharmacy() == null? null : pharmacist.getPharmacy().getId(), rateable, patientRating);
    }

    public List<PharmacistDTO> convert(List<Pharmacist> pharmacists) {
        return pharmacists.stream().map(this::convert).collect(Collectors.toList());
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
