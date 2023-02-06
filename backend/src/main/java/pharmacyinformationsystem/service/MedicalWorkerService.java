package pharmacyinformationsystem.service;

import pharmacyinformationsystem.model.users.MedicalWorker;

public interface MedicalWorkerService extends GenericService<MedicalWorker>{
    Boolean setBusy(Integer id);
    Boolean setFree(Integer id);
}
