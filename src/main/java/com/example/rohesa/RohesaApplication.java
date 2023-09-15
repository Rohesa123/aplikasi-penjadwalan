package com.example.rohesa;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.Date;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableScheduling
public class RohesaApplication implements ApplicationRunner {
	private static final Logger logger = LogManager.getLogger(RohesaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RohesaApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
		logger.info("Spring boot application running in Asia/Jakarta timezone :" + new Date());
	}	

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		logger.info("Version: " + SpringVersion.getVersion());
		logger.info("Application Started ..");
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		logger.info("Custom Jackson ..");
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
	}	
}
