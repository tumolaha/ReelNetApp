package com.learning.reelnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;


@SpringBootApplication
@EnableScheduling // Enable scheduled tasks
public class ReelnetApplication {
	private static final Logger log = LoggerFactory.getLogger(ReelnetApplication.class);
	public static void main(String[] args) {
		// Setup environment files before Spring starts
		ConfigurableApplicationContext context = SpringApplication.run(ReelnetApplication.class, args);

		// Log active profiles
		String[] profiles = context.getEnvironment().getActiveProfiles();
		log.info("Active profiles: {}", Arrays.toString(profiles));

		// Log configuration properties
		log.info("Database URL: {}", context.getEnvironment().getProperty("spring.datasource.url"));
	}
	
	
}
