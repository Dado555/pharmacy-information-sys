package pharmacyinformationsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pharmacyinformationsystem.model.AppointmentReport;

@Repository
public interface AppointmentReportRepository extends JpaRepository<AppointmentReport, Integer> {

}
