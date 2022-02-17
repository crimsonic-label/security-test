package pl.atd.securitytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = "pl.atd.securitytest.rememberme")
public class SecurityTestsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityTestsApplication.class, args);
	}

}
