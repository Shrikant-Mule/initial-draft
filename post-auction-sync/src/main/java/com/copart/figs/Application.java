package com.copart.figs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@ImportResource ("figs-auctions-context.xml")
@EnableCouchbaseRepositories
public class Application {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addListeners(new ApplicationPidListener("figs-auction-results.pid"));
		springApplication.run(args);
		
	}

}
