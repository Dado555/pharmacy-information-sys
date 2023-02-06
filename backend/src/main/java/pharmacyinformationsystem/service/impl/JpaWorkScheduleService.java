package pharmacyinformationsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pharmacyinformationsystem.model.Pharmacy;
import pharmacyinformationsystem.model.WorkSchedule;
import pharmacyinformationsystem.model.users.Dermatologist;
import pharmacyinformationsystem.repository.DermatologistRepository;
import pharmacyinformationsystem.repository.PharmacyRepository;
import pharmacyinformationsystem.repository.WorkScheduleRepository;
import pharmacyinformationsystem.service.WorkScheduleService;

import java.util.List;

@Service
public class JpaWorkScheduleService implements WorkScheduleService {
    private final WorkScheduleRepository workScheduleRepository;
    private final PharmacyRepository pharmacyRepository;
    private final DermatologistRepository dermatologistRepository;

    @Autowired
    public JpaWorkScheduleService(WorkScheduleRepository workScheduleRepository,
                                  PharmacyRepository pharmacyRepository,
                                  DermatologistRepository dermatologistRepository) {
        this.workScheduleRepository = workScheduleRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.dermatologistRepository = dermatologistRepository;
    }

    @Override
    public List<WorkSchedule> findAll() {
        return workScheduleRepository.findAll();
    }

    @Override
    public WorkSchedule findOne(Integer id) {
        return this.workScheduleRepository.findById(id).orElse(null);
    }

    @Override
    public WorkSchedule save(WorkSchedule entity) {
        return workScheduleRepository.save(entity);
    }

    @Override
    public void createNew(Integer pharmacyId, Integer dermatologistId, Long start, Long end) {
        Dermatologist dermatologist = dermatologistRepository.getOne(dermatologistId);
        Pharmacy pharmacy = pharmacyRepository.getOne(pharmacyId);
        WorkSchedule ws = new WorkSchedule(start, end, pharmacy, dermatologist);
        workScheduleRepository.save(ws);
    }
}
