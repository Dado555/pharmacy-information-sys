package pharmacyinformationsystem.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import pharmacyinformationsystem.model.Appointment;
import pharmacyinformationsystem.model.enums.AppointmentStatus;
import pharmacyinformationsystem.model.enums.AppointmentType;
import pharmacyinformationsystem.model.users.Patient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointmentServiceOptimisticLockTest {

    @Autowired
    private AppointmentService appointmentService;

    @Before
    public void setUp() throws Exception {
        appointmentService.save(new Appointment(1L, 2L, 300.0, AppointmentStatus.FREE, AppointmentType.EXAMINATION, null, null, null));
        appointmentService.save(new Appointment(1L, 2L, 300.0, AppointmentStatus.FREE, AppointmentType.EXAMINATION, null, null, null));
    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testScheduleAppointmentExaminationOptimisticLockingScenario() throws Throwable {
        Patient patient1 = new Patient(); patient1.setId(1);
        Patient patient2 = new Patient(); patient2.setId(2);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                Appointment appointment = appointmentService.findOne(1);
                appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
                try { Thread.sleep(3000); } catch (InterruptedException ignored) {}// thread uspavan na 3 sekunde da bi drugi thread mogao da izvrsi istu operaciju
                appointmentService.save(appointment);// bacice ObjectOptimisticLockingFailureException
            }
        });
        executor.submit(new Runnable() {

            @Override
            public void run() {
                Appointment appointment = appointmentService.findOne(1);
                appointment.setAppointmentStatus(AppointmentStatus.RESERVED);
                appointmentService.save(appointment);
            }
        });
        try {
            future1.get(); // podize ExecutionException za bilo koji izuzetak iz prvog child threada
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass()); // u pitanju je bas ObjectOptimisticLockingFailureException
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }
}
