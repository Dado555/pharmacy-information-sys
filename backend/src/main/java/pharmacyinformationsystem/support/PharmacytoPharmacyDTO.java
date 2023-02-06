package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.service.RatingService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.web.dto.domain.PharmacyDTO;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PharmacytoPharmacyDTO implements Converter<Pharmacy, PharmacyDTO> {

    private final RatingService ratingService;
    private final UserService userService;
    private final AddressToAddressDTO addressToAddressDTO;
    private final PointToPointDTO pointToPointDTO;

    @Autowired
    public PharmacytoPharmacyDTO(RatingService ratingService, UserService userService, AddressToAddressDTO addressToAddressDTO, PointToPointDTO pointToPointDTO) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.addressToAddressDTO = addressToAddressDTO;
        this.pointToPointDTO = pointToPointDTO;
    }

    @Override
    public PharmacyDTO convert(Pharmacy pharmacy) {
        boolean rateable;
        try { rateable = ratingService.isPharmacyRateable(pharmacy.getId(), userService.getAuthenticatedUser().getId()); }
        catch(Exception e) { rateable = false; }

        Integer patientRating;
        if (userService.getAuthenticatedUser() != null) {
            Rating rating = ratingService.findRatingByPatientAndPharmacy(userService.getAuthenticatedUser().getId(), pharmacy.getId());
            patientRating = (rating != null) ? rating.getValue() : null;
        } else {
            patientRating = null;
        }

        return new PharmacyDTO(pharmacy.getId(), pharmacy.getName(),
                        addressToAddressDTO.convert(pharmacy.getAddress()),
                        pharmacy.getRating(), pharmacy.getStartWorkTime(),
                        pharmacy.getEndWorkTime(),
                        pointToPointDTO.convert(pharmacy.getPoint()),
                        pharmacy.getCounselingPrice(),
                        rateable, patientRating);
    }

    public List<PharmacyDTO> convert(List<Pharmacy> pharmacyList) {
        return pharmacyList.stream().map(this::convert).collect(Collectors.toList());
    }
}
