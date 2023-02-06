package pharmacyinformationsystem.service.base;

import pharmacyinformationsystem.model.ratings.Rateable;
import pharmacyinformationsystem.model.ratings.Rating;
import pharmacyinformationsystem.model.users.MedicalWorker;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class WorkerServiceBase {

    public Double getAverageRating(Rateable rateable) {
        MedicalWorker worker = (MedicalWorker) rateable;
        double average = 0.0;
        if(worker.getRatings().isEmpty())
            return average;
        try {
            average = worker.getRatings().stream().map(Rating::getValue).mapToDouble(v -> v).average().getAsDouble();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
        return average;
    }

    public List<MedicalWorker> filterByAverageRating(List<MedicalWorker> worker, Integer ratingFrom, Integer ratingTo) {
        return worker.stream()
                .filter(m -> getAverageRating(m) >= ratingFrom && getAverageRating(m) <= ratingTo)
                .collect(Collectors.toList());
    }
}
