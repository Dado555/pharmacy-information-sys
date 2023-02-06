package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.*;
import pharmacyinformationsystem.model.enums.MedicineReservationStatus;
import pharmacyinformationsystem.model.ratings.PharmacyRating;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.repository.*;
import pharmacyinformationsystem.service.PharmacyService;
import pharmacyinformationsystem.service.base.PharmacyServiceBase;
import pharmacyinformationsystem.web.dto.searchparameters.PharmacySearchParameters;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaPharmacyService extends PharmacyServiceBase implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final AddressRepository addressRepository;
    private final DermacyRepository dermacyRepository;

    @Autowired
    public JpaPharmacyService(PharmacyRepository pharmacyRepository, AddressRepository addressRepository,
                              DermacyRepository dermacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
        this.addressRepository = addressRepository;
        this.dermacyRepository = dermacyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmacy> findAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pharmacy> findAll(Integer page) {
        return pharmacyRepository.findAll(PageRequest.of(page, 8));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pharmacy> searchByCriteria(PharmacySearchParameters searchParameters, Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        if (!searchParameters.getSortBy().equals("") && searchParameters.getSortBy() != null) {
            Sort sort = searchParameters.getSortBy().equals("city") ? Sort.by("address.city") : Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(page.orElse(0), 8, sort);
        }
        return pharmacyRepository.findByNameAndCity(searchParameters.getName(), searchParameters.getCity(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Pharmacy findOne(Integer id) {
        return pharmacyRepository.findById(id).orElse(null);
    }

    @Override
    public Pharmacy save(Pharmacy entity) {
        if (entity == null)
            return null;

        return pharmacyRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmacist> findPharmacistsByDateAndTimeCriteria(Integer id, Long start, Long end) {
        Pharmacy pharmacy = findOne(id);
        if (pharmacy == null)
            throw new NotFoundException("Pharmacy not found!");

        return  pharmacy.getPharmacists().stream()
                                         .filter(pharmacist -> {
                                             boolean satisfiesCriteria = true;
                                             for (Appointment a : pharmacist.getAppointmentList()) {
                                                 if (start >= a.getStartDateAndTime() && end <= a.getEndDateAndTime())
                                                     satisfiesCriteria = false;
                                                 if (start < a.getStartDateAndTime() && end >= a.getStartDateAndTime())
                                                     satisfiesCriteria = false;
                                             }
                                             return satisfiesCriteria;
                                         })
                                         .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmacy> findAllByDateAndTimeCriteria(Long startDate, Long startTime, Long endTime) {
        return findAll().stream()
                        .filter(pharmacy -> {
                            if (startTime < pharmacy.getStartWorkTime() * 60000 || startTime > pharmacy.getEndWorkTime() * 60000 || endTime > pharmacy.getEndWorkTime() * 60000)
                                return false;

                            long start = startDate + startTime;
                            long end = startDate + endTime;
                            if (end <= start)
                                return false;

                            if (start < System.currentTimeMillis())
                                return false;

                            for (Pharmacist pharmacist: pharmacy.getPharmacists()) {
                                boolean satisfiesCriteria = true;
                                for (Appointment a : pharmacist.getAppointmentList()) {
                                    if (start >= a.getStartDateAndTime() && end <= a.getEndDateAndTime()) {
                                        satisfiesCriteria = false;
                                        break;
                                    }
                                    if (start < a.getStartDateAndTime() && end >= a.getStartDateAndTime()) {
                                        satisfiesCriteria = false;
                                        break;
                                    }
                                }
                                if (satisfiesCriteria)
                                    return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());
    }

    @Override
    public List<Pharmacy> filterByAverageRating(List<Pharmacy> pharmacies, Integer ratingFrom, Integer ratingTo) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Address findAddressById(Integer id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public void updateName(Integer id, String name) {
        try{
            pharmacyRepository.updateName(id, name);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new BadRequestException("Someone is already updating pharmacy name... Try again soon");
        }
    }

    @Override
    public void updateAddress(Integer id, Integer addressId) {
        try{
            pharmacyRepository.updateAddress(id, addressId);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new BadRequestException("Someone is already updating pharmacy address... Try again soon");
        }
    }

    @Override
    public List<PharmacyMedicine> getPharmacyMedicines(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return new ArrayList<>(pharmacy.getPharmacyMedicineList());
    }

    @Override
    public Double getAllEarnings(Integer id) {
        Pharmacy pharmacy = pharmacyRepository.findOneById(id);
        List<PharmacyMedicine> medicines = pharmacy.getPharmacyMedicineList();
        double sum = 0.0;
        for(PharmacyMedicine medicine: medicines) {
            for(MedicineReservationItem item: medicine.getReservationItems()) {
                if(!item.getMedicineReservation().getStatus().equals(MedicineReservationStatus.CANCELED)) {
                    //sum += item.getAmount() * medicine.getPrice();
                    Long reservationDate = item.getMedicineReservation().getDateAndTime();
                    for (PharmacyMedicinePrice price : medicine.getMedicinePrices()) {
                        if (reservationDate > price.getStartDate() &&
                                (price.getEndDate() == null || reservationDate < price.getEndDate())) {
                            Double priceFin = price.getPrice();
                            for (PharmedActionPrice actionPrice : price.getPharmedActionPrices()) {
                                if (reservationDate > actionPrice.getStartDate() && reservationDate < actionPrice.getEndDate()) {
                                    priceFin = actionPrice.getPrice();
                                    break;
                                }
                            }
                            sum += item.getAmount() * priceFin;
                            break;
                        }
                    }
                }
            }
        }
        return sum;
    }

    @Override
    public List<Double> getMonthlyEarnings(Integer id) {
        Pharmacy pharmacy = pharmacyRepository.findOneById(id);
        List<Double> earnings = new ArrayList<>(Collections.nCopies(12, 0.0));
        for(PharmacyMedicine medicine: pharmacy.getPharmacyMedicineList()) {
            List<Double> soldMonthly = soldMonthly(medicine);
            for(int i = 0; i < soldMonthly.size(); i++) {
                earnings.set(i, earnings.get(i) + soldMonthly.get(i));
            }
        }
        return earnings;
    }

    @Override
    public List<Double> getYearlyEarnings(Integer id) {
        Pharmacy pharmacy = pharmacyRepository.findOneById(id);
        List<Double> earnings = new ArrayList<>(Collections.nCopies(10, 0.0));
        for(PharmacyMedicine medicine: pharmacy.getPharmacyMedicineList()) {
            List<Double> soldYearly = soldYearly(medicine);
            for(int i = 0; i < soldYearly.size(); i++) {
                earnings.set(i, earnings.get(i) + soldYearly.get(i));
            }
        }
        return earnings;
    }

    @Override
    public List<Dermatologist> getDermatologists(Integer id) {
        Pharmacy pharmacy = findOne(id);
        List<Dermatologist> dermatologists = new ArrayList<>(pharmacy.getDermatologists());
        for(Dermatologist d: dermatologists) {
            d.setDermacy(new ArrayList<>(dermacyRepository.findAllByDermatologistId(d.getId())));
        }
        return dermatologists;
    }

    @Override
    public List<Pharmacist> getPharmacists(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return new ArrayList<>(pharmacy.getPharmacists());
    }

    @Override
    public List<Order> getOrders(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return new ArrayList<>(pharmacy.getOrders());
    }

    @Override
    public List<TimeOffRequest> getTimeOffs(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return new ArrayList<>(pharmacy.getTimeOffRequests());
    }

    @Override
    public List<MedicineInquiry> getMedicineInquiries(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return new ArrayList<>(pharmacy.getMedicineInquiries());
    }

    @Override
    public List<Double> getMonthlyRatings(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return ratingsMonthly(pharmacy.getRatings());
    }

    @Override
    public List<Double> getYearlyRatings(Integer id) {
        Pharmacy pharmacy = findOne(id);
        return ratingsYearly(pharmacy.getRatings());
    }

    @Override
    public List<Subscription> getSubs(Integer id) {
        Pharmacy pharmacy = pharmacyRepository.getOne(id);
        return new ArrayList<>(pharmacy.getSubscriptions());
    }

    @Override
    public Pharmacy savePharmacy(Pharmacy pharmacy) {
        Address address = pharmacy.getAddress();
        address.setLongitude(0.0);
        address.setLatitude(0.0);
        pharmacy.setRating(0.0);
        return save(pharmacy);
    }

    private List<Double> soldMonthly(PharmacyMedicine pharmacyMedicine) {
        List<List<MedicineReservationItem>> separated = getLastYear(pharmacyMedicine.getReservationItems());
        return soldStats(separated, pharmacyMedicine);
    }

    private List<Double> soldYearly(PharmacyMedicine pharmacyMedicine) {
        List<List<MedicineReservationItem>> separated = getLastTenYears(pharmacyMedicine.getReservationItems());
        return soldStats(separated, pharmacyMedicine);
    }

    private List<Double> soldStats(List<List<MedicineReservationItem>> separated, PharmacyMedicine pharmacyMedicine) {
        List<Double> sums = new ArrayList<>();
        for(List<MedicineReservationItem> itemList: separated) {
            double sum = 0.0;
            for(MedicineReservationItem item: itemList) {
                Long reservationDate = item.getMedicineReservation().getDateAndTime();
                for(PharmacyMedicinePrice price: pharmacyMedicine.getMedicinePrices()) {
                    if(reservationDate > price.getStartDate() &&
                            (price.getEndDate() == null || reservationDate < price.getEndDate()))
                    {
                        Double priceFin = price.getPrice();
                        for(PharmedActionPrice actionPrice: price.getPharmedActionPrices())
                        {
                            if(reservationDate > actionPrice.getStartDate() && reservationDate < actionPrice.getEndDate())
                            {
                                priceFin = actionPrice.getPrice();
                                break;
                            }
                        }
                        sum += item.getAmount() * priceFin;
                        break;
                    }
                }
            }
            sums.add(sum);
        }
        return sums;
    }

    private List<List<MedicineReservationItem>> getLastYear(List<MedicineReservationItem> items) {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        Calendar start = Calendar.getInstance();
        start.add(Calendar.YEAR, -1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        int i = (start.get(Calendar.MONTH) + 1) %12;
        while(counter < 12) {
            map.put(i, counter);
            counter++;
            i++;
            if(i == 12)
                i = 0;
        }
        curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
        return filterList(start, curr, items, true, map);
    }

    private List<List<MedicineReservationItem>> getLastTenYears(List<MedicineReservationItem> items) {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        Calendar start = Calendar.getInstance();
        start.set(curr.get(Calendar.YEAR)-9, 0, 1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        for(int i = start.get(Calendar.YEAR); i <= curr.get(Calendar.YEAR); i++, counter++)
            map.put(i, counter);
        curr.set(Calendar.DAY_OF_MONTH, curr.getActualMaximum(Calendar.DAY_OF_MONTH));
        return filterList(start, curr, items, false, map);
    }

    private List<List<MedicineReservationItem>> filterList(Calendar start, Calendar end, List<MedicineReservationItem> items,
                                                           boolean month, Map<Integer, Integer> map) {
        List<List<MedicineReservationItem>> separated = new ArrayList<>();
        if(month) {
            for(int i = 0; i < 12; i++)
                separated.add(new ArrayList<>());
        }
        else {
            for(int i = 0; i < 10; i++)
                separated.add(new ArrayList<>());
        }

        for(MedicineReservationItem item: items) {
            Calendar curr = Calendar.getInstance();
            curr.setTimeInMillis(item.getMedicineReservation().getDateAndTime());
            if(!item.getMedicineReservation().getStatus().equals(MedicineReservationStatus.CANCELED)) {
                if (curr.after(start) && curr.before(end)) {
                    if (month)
                        separated.get(map.get(curr.get(Calendar.MONTH))).add(item);
                    else
                        separated.get(map.get(curr.get(Calendar.YEAR))).add(item);
                }
            }
        }
        return separated;
    }

    private List<List<PharmacyRating>> getLastYear2(List<PharmacyRating> ratings){
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.setTimeInMillis(System.currentTimeMillis());
        Calendar startDateTime = Calendar.getInstance();
        System.out.println("Trenutna godina: " + currentDateTime.get(Calendar.YEAR));
        startDateTime.add(Calendar.YEAR, -1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        int i = (startDateTime.get(Calendar.MONTH) + 1) % 12;
        while(counter < 12){
            map.put(i, counter);
            counter++;
            i++;
            if(i==12)
                i=0;
        }
        return filterList2(startDateTime, currentDateTime, ratings, true, map);
    }

    private List<List<PharmacyRating>> getLastTenYears2(List<PharmacyRating> ratings) {
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.setTimeInMillis(System.currentTimeMillis());
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(currentDateTime.get(Calendar.YEAR)-9, 0, 1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        for(int i = startDateTime.get(Calendar.YEAR); i <= currentDateTime.get(Calendar.YEAR); i++, counter++)
            map.put(i, counter);
        return filterList2(startDateTime, currentDateTime, ratings, false, map);
    }

    private List<List<PharmacyRating>> filterList2(Calendar start, Calendar end, List<PharmacyRating> ratings, boolean month, Map<Integer, Integer> map) {
        List<List<PharmacyRating>> separated = new ArrayList<>();
        if(month) {
            for (int i = 0; i < 12; i++)
                separated.add(new ArrayList<>());
        }
        else {
            for(int i = 0; i < 10; i++)
                separated.add(new ArrayList<>());
        }

        for(PharmacyRating pr: ratings) {
            Calendar current = Calendar.getInstance();
            current.setTimeInMillis(pr.getDateTime());
            if(current.after(start) && current.before(end)) {
                if(month)
                    separated.get(map.get(current.get(Calendar.MONTH))).add(pr);
                else
                    separated.get(map.get(current.get(Calendar.YEAR))).add(pr);
            }
        }
        return separated;
    }

    private List<Double> ratingsMonthly(List<PharmacyRating> ratings){
        List<List<PharmacyRating>> separated = getLastYear2(ratings);
        List<Double> calculated = new ArrayList<>();
        for(List<PharmacyRating> rat: separated){
            calculated.add(calculateRating(rat));
        }
        return calculated;
    }

    private List<Double> ratingsYearly(List<PharmacyRating> ratings) {
        List<List<PharmacyRating>> separated = getLastTenYears2(ratings);
        List<Double> calculated = new ArrayList<>();
        for(List<PharmacyRating> rat: separated){
            calculated.add(calculateRating(rat));
        }
        return calculated;
    }

    private Double calculateRating(List<PharmacyRating> ratings) {
        double rating = 0.0;
        for(PharmacyRating pt : ratings) {
            rating += pt.getValue().doubleValue();
        }
        if(rating > 0) return rating/ratings.size();
        return rating;
    }
}
