package pharmacyinformationsystem.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pharmacyinformationsystem.model.Medicine;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.service.RatingService;
import pharmacyinformationsystem.service.UserService;
import pharmacyinformationsystem.web.dto.domain.MedicineDTO;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MedicineToMedicineDTO implements Converter<Medicine, MedicineDTO> {

    private final RatingService ratingService;
    private final UserService userService;

    @Autowired
    public MedicineToMedicineDTO(RatingService ratingService, UserService userService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }

    @Override
    public MedicineDTO convert(Medicine medicine) {
        boolean rateable;
        try { rateable = ratingService.isMedicineRateable(medicine.getId(), userService.getAuthenticatedUser().getId()); }
        catch(Exception e) { rateable = false; }

        Integer patientRating;
        if (userService.getAuthenticatedUser() != null) {
            Rating rating = ratingService.findRatingByPatientAndMedicine(userService.getAuthenticatedUser().getId(), medicine.getId());
            patientRating = (rating != null) ? rating.getValue() : null;
        } else {
            patientRating = null;
        }

        return new MedicineDTO(medicine.getId(),
                                medicine.getName(),
                                medicine.getType(),
                                medicine.getMedicineForm(),
                                medicine.getStructure(),
                                medicine.getManufacture(),
                                medicine.getMedicineIssuingType(),
                                medicine.getDailyIntake(),
                                medicine.getContraindications(),
                                medicine.getRating(),
                                rateable, patientRating);
    }

    public List<MedicineDTO> convert(List<Medicine> medicineList) {
        return medicineList.stream().map(this::convert).collect(Collectors.toList());
    }
}