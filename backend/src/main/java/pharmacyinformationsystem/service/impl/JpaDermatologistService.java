package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.Address;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.Order;
import pharmacyinformationsystem.model.WorkSchedule;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.ratings.MedicalWorkerRating;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.model.users.Role;
import pharmacyinformationsystem.model.users.User;
import pharmacyinformationsystem.repository.AppointmentRepository;
import pharmacyinformationsystem.repository.DermatologistRepository;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.service.AddressService;
import pharmacyinformationsystem.service.DermatologistService;
import pharmacyinformationsystem.service.RoleService;
import pharmacyinformationsystem.web.dto.domain.AppointmentDTO;
import pharmacyinformationsystem.web.dto.domain.WorkScheduleDTO;
import pharmacyinformationsystem.web.dto.searchparameters.MedicalWorkerSearchParameters;
import pharmacyinformationsystem.service.base.WorkerServiceBase;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaDermatologistService extends WorkerServiceBase implements DermatologistService {

    private final DermatologistRepository dermatologistRepository;
    private final PharmacyRepository pharmacyRepository;
    private final AppointmentRepository appointmentRepository;
    //@PersistenceContext
    //private EntityManager entityManager;

    private final AddressService addressService;
    private final RoleService roleService;

    @Autowired
    public JpaDermatologistService(DermatologistRepository dermatologistRepository, PharmacyRepository pharmacyRepository,
                                   AppointmentRepository appointmentRepository, AddressService addressService,
                                   RoleService roleService) {
        this.dermatologistRepository = dermatologistRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.appointmentRepository = appointmentRepository;
        this.addressService = addressService;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dermatologist> findAll() {
        return dermatologistRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Dermatologist> findAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 8);
        return dermatologistRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Dermatologist findOne(Integer id) {
        return this.dermatologistRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Dermatologist save(Dermatologist entity) {
        if (entity == null)
            return null;

        return this.dermatologistRepository.save(entity);
    }

    @Override
    public List<List<Long>> getTakenAppointments(Integer derId) {
        Dermatologist dermatologist = findOne(derId);
        return takenAppointments(dermatologist);
    }

    @Override
    public List<Double> getRatingsMonthly(Integer id) {
        Dermatologist dermatologist = findOne(id);
        return ratingsMonthly(dermatologist.getRatings());
    }

    @Override
    public List<Double> getRatingsYearly(Integer id) {
        Dermatologist dermatologist = findOne(id);
        return ratingsYearly(dermatologist.getRatings());
    }

    @Override
    public List<Integer> getAppointmentsMonthly(Integer id) {
        Dermatologist dermatologist = findOne(id);
        return doneAppointmentsMonthly(dermatologist.getAppointmentList());
    }

    @Override
    public List<Integer> getAppointmentsYearly(Integer id) {
        Dermatologist dermatologist = findOne(id);
        return doneAppointmentsYearly(dermatologist.getAppointmentList());
    }

    @Override
    public WorkScheduleDTO getWorkSchedule(Integer pharmacyId, Integer dermatologistId) {
        Dermatologist dermatologist = findOne(dermatologistId);
        WorkScheduleDTO workScheduleDTO = null;
        for(WorkSchedule ws: dermatologist.getWorkScheduleList()) {
            if(ws.getPharmacy().getId().equals(pharmacyId))
                workScheduleDTO = new WorkScheduleDTO(ws.getId(), ws.getMedicalWorker().getId(),
                        ws.getPharmacy().getId(), ws.getStartTime(), ws.getEndTime());
        }
        return workScheduleDTO;
    }

    @Override
    public Boolean createAppointment(AppointmentDTO appointmentDTO) {
        Dermatologist dermatologist = findOne(appointmentDTO.getWorkerId());
        try{
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
        app.setMedicalWorker(dermatologist);
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
    public List<Appointment> getFutureAppointments(Integer pharmacyId, Integer dermatologistId) {
        Dermatologist dermatologist = findOne(dermatologistId);
        List<Appointment> retList = new ArrayList<>();
        for (Appointment app : dermatologist.getAppointmentList()){
            if(app.getAppointmentStatus().equals(AppointmentStatus.RESERVED) &&
                    app.getPharmacy().getId().equals(pharmacyId) &&
                    app.getStartDateAndTime() > System.currentTimeMillis()) {
                retList.add(app);
            }
        }
        return retList;
    }

    @Override
    public Page<Dermatologist> searchByCriteria(MedicalWorkerSearchParameters searchParameters, Optional<Integer> page) {
        Pageable pageable;
        try {
            Sort sort = searchParameters.getSortBy().equals("address") ?  Sort.by("address.city") : Sort.by(searchParameters.getSortBy());
            sort = searchParameters.getOrderBy().equals("descending") ? sort.descending() : sort.ascending();
            pageable = PageRequest.of(page.orElse(0), 8, sort);
        } catch (Exception e) {
            pageable = PageRequest.of(page.orElse(0), 8);
        }
        Page<Dermatologist> dermatologistsPage = dermatologistRepository.findAllBySearchCriteria(searchParameters.getFirstName(), searchParameters.getLastName(),
                                                               searchParameters.getPhoneNumber(), searchParameters.getAddress(),
                                                               searchParameters.getPharmacyIds() == null ? 0 : searchParameters.getPharmacyIds().size(),
                                                               searchParameters.getPharmacyIds(), pageable);
        List<Dermatologist> dermatologists = (List<Dermatologist>)(List<?>)filterByAverageRating((List<MedicalWorker>)(List<?>)
                dermatologistsPage.toList(), searchParameters.getRatingFrom(), searchParameters.getRatingTo());

        if (searchParameters.getSortBy().equals("rating")) {
            dermatologists = dermatologists.stream().sorted(Comparator.comparing(this::getAverageRating)).collect(Collectors.toList());
            if (searchParameters.getOrderBy() != null && searchParameters.getOrderBy().equals("descending"))
                Collections.reverse(dermatologists);
        }

        pageable = PageRequest.of(page.orElse(0), 8);
        int pageSize = pageable.getPageSize();
        long pageOffset = pageable.getOffset();
        long total = pageOffset + dermatologists.size() + (dermatologists.size() == pageSize ? pageSize : 0);
        return new PageImpl<>(dermatologists, pageable, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dermatologist> findEmployable(Integer pharmacyId) {
        return dermatologistRepository.findEmployable(pharmacyId);
    }

    @Override
    @Transactional(readOnly = true)
    public Dermatologist findDermatologistByEmail(String email) {
        return dermatologistRepository.findDermatologistByEmail(email);
    }

    @Override
    public User update(Dermatologist dermatologist, Integer id) {
        Dermatologist user = findOne(id);
        if (user == null)
            throw new NotFoundException("Dermatologist not found!");

        user.update(dermatologist.getFirstName(), dermatologist.getLastName(), dermatologist.getPhoneNumber());
        return save(user);
    }

    @Override
    public List<Appointment> getAppointments(Integer id) {
        Dermatologist dermatologist = findOne(id);
        if (dermatologist == null)
            throw new NotFoundException("Dermatologist not found!");
        return new ArrayList<>(dermatologist.getAppointmentList());
    }


    private List<List<Long>> takenAppointments(Dermatologist dermatologist) {
        List<List<Long>> list = new ArrayList<>();
        for(WorkSchedule ws: dermatologist.getWorkScheduleList()) {
            List<Long> arr = new ArrayList<>();
            arr.add(ws.getStartTime());
            arr.add(ws.getEndTime());
            list.add(arr);
        }
        return list;
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
    public Dermatologist deleteById(Integer id) {
        Dermatologist dermatologist = findOne(id);
        if (dermatologist == null)
            throw new NotFoundException("Dermatologist not found!");

        dermatologist.setActive(true);
        save(dermatologist);
        return dermatologist;
    }

    @Override
    public Dermatologist createDermatologist(Dermatologist dermatologist) {
        if (findDermatologistByEmail(dermatologist.getEmail()) != null) {
            return null;
        }

        Address address = addressService.save(dermatologist.getAddress());
        dermatologist.setAddress(address);
        dermatologist.setActive(true);
        Role role = roleService.findByName("ROLE_DERMATOLOGIST");
        dermatologist.setRole(role);
        Dermatologist savedDermatologist = save(dermatologist);
        savedDermatologist.setDermacy(null);
        return savedDermatologist;
    }
}
