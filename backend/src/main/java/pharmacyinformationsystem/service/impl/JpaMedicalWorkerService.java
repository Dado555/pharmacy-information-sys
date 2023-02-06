package pharmacyinformationsystem.service.impl;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pharmacyinformationsystem.model.users.MedicalWorker;
import pharmacyinformationsystem.repository.MedicalWorkerRepository;
import pharmacyinformationsystem.service.MedicalWorkerService;
import pharmacyinformationsystem.web.exception.BadRequestException;
import pharmacyinformationsystem.web.exception.NotFoundException;

import java.util.List;

@Service
@Transactional
public class JpaMedicalWorkerService implements MedicalWorkerService {

    private final MedicalWorkerRepository medicalWorkerRepository;

    public JpaMedicalWorkerService(MedicalWorkerRepository medicalWorkerRepository) {
        this.medicalWorkerRepository = medicalWorkerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalWorker> findAll() {
        return medicalWorkerRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalWorker findOne(Integer id) {
        return medicalWorkerRepository.findById(id).orElse(null);
    }

    @Override
    public MedicalWorker save(MedicalWorker entity) {
        return medicalWorkerRepository.save(entity);
    }

    @Override
    public Boolean setBusy(Integer id) {
        MedicalWorker mw = findOne(id);
        if (mw == null) {
            throw new NotFoundException("MedicalWorker does not exist.");
        }

        if (mw.getHasActiveAppointment()) {
            throw new BadRequestException("Appointment is already active.");
        }

        try {
            mw.setHasActiveAppointment(true);
            save(mw);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Your account is currently busy. Try again later.");
        }
        return true;
    }

    @Override
    public Boolean setFree(Integer id) {
        MedicalWorker mw = findOne(id);
        if (mw == null) {
            throw new NotFoundException("MedicalWorker does not exist.");
        }

        if (!mw.getHasActiveAppointment()) {
            throw new BadRequestException("Appointment is not active.");
        }

        try {
            mw.setHasActiveAppointment(false);
            save(mw);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Your account is currently busy. Try again later.");
        }
        return true;
    }
}
