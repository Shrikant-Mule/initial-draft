package com.copart.emailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan
@Profile("errorsender")
@ImportResource("classpath:notification-sender-context.xml")
public class ErrorSenderConfiguration {
	 public static void main(String[] args) {
			SpringApplication springApplication = new SpringApplication(ErrorSenderConfiguration.class);
			springApplication.addListeners(new ApplicationPidListener("error-email-sender-" +  "auction" + ".pid"));
			springApplication.run(args);
	    }
}
