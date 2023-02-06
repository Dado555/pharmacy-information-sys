package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.WorkSchedule;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.users.Pharmacist;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.repository.AppointmentRepository;
import pharmacyinformationsystem.repository.PharmacistRepository;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.service.PharmacistService;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;

import org.springframework.data.domain.*;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.service.base.WorkerServiceBase;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;


import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class JpaPharmacistService extends WorkerServiceBase implements PharmacistService {

    private final PharmacistRepository pharmacistRepository;
    private final PharmacyRepository pharmacyRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public JpaPharmacistService(PharmacistRepository pharmacistRepository, PharmacyRepository pharmacyRepository, AppointmentRepository appointmentRepository){
        this.pharmacistRepository = pharmacistRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pharmacist> findAll() {
        return pharmacistRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Pharmacist findOne(Integer id) {
        return this.pharmacistRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Pharmacist findOneWithLock(Integer id) { return this.pharmacistRepository.findOneByIdWithLock(id); }

    @Override
    public Pharmacist save(Pharmacist entity) {
        if(entity == null)
            return null;

        return this.pharmacistRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pharmacist> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return pharmacistRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pharmacist> searchByCriteria(MedicalWorkerSearchParameters searchParameters, Optional<Integer> page) {
        Pageable pageable;
        try {
            Sort sort = searchParameters.getSortBy().equals("address") ?  Sort.by("address.city") : Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(page.orElse(0), 8, sort);
        } catch (Exception e) {
            pageable = PageRequest.of(page.orElse(0), 8);
        }
        Page<Pharmacist> pharmacistsPage = pharmacistRepository.findAllBySearchCriteria(searchParameters.getFirstName(), searchParameters.getLastName(),
                                                            searchParameters.getPhoneNumber(), searchParameters.getAddress(),
                                                            searchParameters.getPharmacyIds() == null ? 0 : searchParameters.getPharmacyIds().size(),
                                                            searchParameters.getPharmacyIds(), pageable);
        List<Pharmacist> pharmacists = (List<Pharmacist>)(List<?>)filterByAverageRating((List<MedicalWorker>)(List<?>)
                pharmacistsPage.toList(), searchParameters.getRatingFrom(), searchParameters.getRatingTo());

        if (searchParameters.getSortBy().equals("rating")) {
            pharmacists = pharmacists.stream().sorted(Comparator.comparing(this::getAverageRating)).collect(Collectors.toList());
            if (searchParameters.getOrderBy() != null && searchParameters.getOrderBy().equals("descending"))
                Collections.reverse(pharmacists);
        }

        pageable = PageRequest.of(page.orElse(0), 8);
        int pageSize = pageable.getPageSize();
        long pageOffset = pageable.getOffset();
        long total = pageOffset + pharmacists.size() + (pharmacists.size() == pageSize ? pageSize : 0);
        return new PageImpl<>(pharmacists, pageable, total);
    }

    @Override
    public void pharmacistDismissal(Integer id) {
        pharmacistRepository.pharmacistDismissal(id);
    }

    @Override
    public List<Pharmacist> findAllByPharmacyIsNull() {
        return pharmacistRepository.findAllByPharmacyIsNull();
    }

    @Override
    public void hirePharmacist(Integer id, Integer pharmacyId) {
        pharmacistRepository.hirePharmacist(id, pharmacyId);
    }

    @Override
    public User update(Pharmacist pharmacist, Integer id) {
        Pharmacist user = findOne(id);
        if (user == null)
            throw new NotFoundException("Pharmacist not found!");

        user.update(pharmacist.getFirstName(), pharmacist.getLastName(), pharmacist.getPhoneNumber());
        return save(user);
    }

    @Override
    public List<Appointment> getAppointments(Integer id) {
        Pharmacist pharmacist = findOne(id);
        if (pharmacist == null)
            throw new NotFoundException("Pharmacist not found!");
        return new ArrayList<>(pharmacist.getAppointmentList());
    }

    @Override
    public List<Double> getRatingsMonthly(Integer id) {
        Pharmacist pharmacist = findOne(id);
        return ratingsMonthly(pharmacist.getRatings());
    }

    @Override
    public List<Double> getRatingsYearly(Integer id) {
        Pharmacist pharmacist = findOne(id);
        return ratingsYearly(pharmacist.getRatings());
    }

    @Override
    public List<Integer> getAppointmentsMonthly(Integer id) {
        Pharmacist pharmacist = findOne(id);
        return doneAppointmentsMonthly(pharmacist.getAppointmentList());
    }

    @Override
    public List<Integer> getAppointmentsYearly(Integer id) {
        Pharmacist pharmacist = findOne(id);
        return doneAppointmentsYearly(pharmacist.getAppointmentList());
    }

    @Override
    public WorkScheduleDTO getWorkSchedule(Integer pharmacyId, Integer pharmacistId) {
        Pharmacist pharmacist = findOne(pharmacistId);
        WorkScheduleDTO workScheduleDTO = null;
        for(WorkSchedule ws: pharmacist.getWorkScheduleList()) {
            if(ws.getPharmacy().getId().equals(pharmacyId))
                workScheduleDTO = new WorkScheduleDTO(ws.getId(), ws.getMedicalWorker().getId(),
                        ws.getPharmacy().getId(), ws.getStartTime(), ws.getEndTime());
        }
        return workScheduleDTO;
    }

    @Override
    public Boolean createAppointment(AppointmentDTO appointmentDTO) {
        Pharmacist pharmacist = findOne(appointmentDTO.getWorkerId());
        if(pharmacist == null)
            return false;

        try {
            List<Appointment> appointments = appointmentRepository.findByMedicalWorkerId(appointmentDTO.getWorkerId());
            for (Appointment app : appointments){
                if(app.getStartDateAndTime() >= appointmentDTO.getStart() && app.getEndDateAndTime() <= appointmentDTO.getEnd()) {
                    System.out.println("Appointment in that time already exist!");
                    return false;
                }
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new BadRequestException("Someone already creating appointment... Try again soon");
        }

        Appointment app = new Appointment();
        app.setStartDateAndTime(appointmentDTO.getStart());
        app.setEndDateAndTime(appointmentDTO.getEnd());
        app.setMedicalWorker(pharmacist);
        app.setPatient(null);
        app.setAppointmentStatus(appointmentDTO.getAppointmentStatus());
        app.setAppointmentType(appointmentDTO.getTitle());
        app.setAppointmentReport(null);
        app.setPrice(appointmentDTO.getPrice());
        app.setPharmacy(pharmacyRepository.getOne(appointmentDTO.getPharmacyId()));

        appointmentRepository.save(app);
        return true;
    }

    @Override
    public List<Appointment> getFutureAppointments(Integer pharmacyId, Integer pharmacistId) {
        Pharmacist pharmacist = findOne(pharmacistId);;
        List<Appointment> retList = new ArrayList<>();
        for (Appointment app : pharmacist.getAppointmentList()){
            if(app.getAppointmentStatus().equals(AppointmentStatus.RESERVED) &&
                    app.getPharmacy().getId().equals(pharmacyId) &&
                    app.getStartDateAndTime() > System.currentTimeMillis()) {
                retList.add(app);
            }
        }
        return retList;
    }

    @Override
    @Transactional(readOnly = true)
    public Pharmacist findPharmacistByEmail(String email) {
        return pharmacistRepository.findPharmacistByEmail(email);
    }

    private List<List<MedicalWorkerRating>> getLastYear(List<MedicalWorkerRating> ratings){
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
        return filterList(startDateTime, currentDateTime, ratings, true, map);
    }

    private List<List<MedicalWorkerRating>> getLastTenYears(List<MedicalWorkerRating> ratings) {
        Calendar currentDateTime = Calendar.getInstance();
        currentDateTime.setTimeInMillis(System.currentTimeMillis());
        Calendar startDateTime = Calendar.getInstance();
        startDateTime.set(currentDateTime.get(Calendar.YEAR)-9, 0, 1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        for(int i = startDateTime.get(Calendar.YEAR); i <= currentDateTime.get(Calendar.YEAR); i++, counter++)
            map.put(i, counter);
        return filterList(startDateTime, currentDateTime, ratings, false, map);
    }

    private List<List<MedicalWorkerRating>> filterList(Calendar start, Calendar end, List<MedicalWorkerRating> ratings, boolean month, Map<Integer, Integer> map) {
        List<List<MedicalWorkerRating>> separated = new ArrayList<>();
        if(month) {
            for (int i = 0; i < 12; i++)
                separated.add(new ArrayList<>());
        }
        else {
            for(int i = 0; i < 10; i++)
                separated.add(new ArrayList<>());
        }

        for(MedicalWorkerRating pr: ratings) {
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

    private List<Double> ratingsMonthly(List<MedicalWorkerRating> ratings){
        List<List<MedicalWorkerRating>> separated = getLastYear(ratings);
        List<Double> calculated = new ArrayList<>();
        for(List<MedicalWorkerRating> rat: separated){
            calculated.add(calculateRating(rat));
        }
        return calculated;
    }

    private List<Double> ratingsYearly(List<MedicalWorkerRating> ratings) {
        List<List<MedicalWorkerRating>> separated = getLastTenYears(ratings);
        List<Double> calculated = new ArrayList<>();
        for(List<MedicalWorkerRating> rat: separated){
            calculated.add(calculateRating(rat));
        }
        return calculated;
    }

    private Double calculateRating(List<MedicalWorkerRating> ratings) {
        double rating = 0.0;
        for(MedicalWorkerRating pt : ratings) {
            rating += pt.getValue().doubleValue();
        }
        if(rating > 0) return rating/ratings.size();
        return rating;
    }

    public List<Integer> doneAppointmentsMonthly(List<Appointment> appointments) {
        List<List<Appointment>> separated = getLastYear2(appointments);
        List<Integer> calculated = new ArrayList<>();
        for(List<Appointment> appList: separated)
            calculated.add(appList.size());
        return calculated;
    }

    public List<Integer> doneAppointmentsYearly(List<Appointment> appointments) {
        List<List<Appointment>> separated = getLastTenYears2(appointments);
        List<Integer> calculated = new ArrayList<>();
        for(List<Appointment> appList: separated)
            calculated.add(appList.size());
        return calculated;
    }

    private List<List<Appointment>> getLastYear2(List<Appointment> appointments) {
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
        return filterList2(start, curr, appointments, true, map);
    }

    private List<List<Appointment>> getLastTenYears2(List<Appointment> appointments) {
        Calendar curr = Calendar.getInstance();
        curr.setTimeInMillis(System.currentTimeMillis());
        Calendar start = Calendar.getInstance();
        start.set(curr.get(Calendar.YEAR)-9, 0, 1);
        Map<Integer, Integer> map = new HashMap<>();
        int counter = 0;
        for(int i = start.get(Calendar.YEAR); i <= curr.get(Calendar.YEAR); i++, counter++)
            map.put(i, counter);
        return filterList2(start, curr, appointments, false, map);
    }

    private List<List<Appointment>> filterList2(Calendar start, Calendar end, List<Appointment> appointments,
                                                boolean month, Map<Integer, Integer> map) {
        List<List<Appointment>> separated = new ArrayList<>();
        if(month) {
            for(int i = 0; i < 12; i++)
                separated.add(new ArrayList<>());
        }
        else {
            for(int i = 0; i < 10; i++)
                separated.add(new ArrayList<>());
        }

        for(Appointment app: appointments) {
            if(app.getAppointmentStatus() == AppointmentStatus.DONE) {
                Calendar curr = Calendar.getInstance();
                curr.setTimeInMillis(app.getStartDateAndTime());
                if (curr.after(start) && curr.before(end)) {
                    if (month)
                        separated.get(map.get(curr.get(Calendar.MONTH))).add(app);
                    else
                        separated.get(map.get(curr.get(Calendar.YEAR))).add(app);
                }
            }
        }
        return separated;
    }

    @Override
    public Pharmacist deleteById(Integer id) {
        Pharmacist pharmacist = findOne(id);
        if (pharmacist == null)
            throw new NotFoundException("Pharmacist not found!");

        save(pharmacist);
        return pharmacist;
    }

}
