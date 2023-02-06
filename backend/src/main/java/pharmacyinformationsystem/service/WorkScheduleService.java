package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.WorkSchedule;

public interface WorkScheduleService extends GenericService<WorkSchedule> {
    void createNew(Integer pharmacyId, Integer dermatologistId, Long start, Long end);
}
