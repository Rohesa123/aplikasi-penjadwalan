package com.example.rohesa;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableScheduling
public class RohesaApplication implements ApplicationRunner {
	private static final Logger logger = LogManager.getLogger(RohesaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RohesaApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		logger.info("Version: " + SpringVersion.getVersion());
		logger.info("Application Started ..");
	}
}
