package pharmacyinformationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan("pharmacyinformationsystem")
@EnableJpaRepositories("pharmacyinformationsystem")
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class PharmacyInformationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmacyInformationSystemApplication.class, args);
	}
}

































