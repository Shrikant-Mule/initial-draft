package com.copart.emailsender;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidListener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource ("email-sender-context.xml")
public class Application {

    public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		String schema = "";
		for (String arg: args) {
			if (arg.toLowerCase().startsWith("--db.schema")) {
				schema = StringUtils.substringAfter(arg.toLowerCase().trim(), "--db.schema=");
			}
		}
		springApplication.addListeners(new ApplicationPidListener("email-sender-" +  schema + ".pid"));
		springApplication.run(args);
    }
    
    

}
