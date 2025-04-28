package com.learning.reelnet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
@EnableScheduling // Enable scheduled tasks
public class ReelnetApplication {

	public static void main(String[] args) {
		// Setup environment files before Spring starts
		setupEnvironmentFiles();
		
		SpringApplication.run(ReelnetApplication.class, args);
	}
	
	/**
	 * Setup environment files before Spring starts.
	 * This ensures environment variables are available during Spring context initialization.
	 */
	private static void setupEnvironmentFiles() {
		String profile = System.getProperty("spring.profiles.active", "dev");
		System.out.println("Setting up environment for profile: " + profile);
		
		try {
			Path sourceEnvPath;
			File envDevFile = new File(".env.development");
			File envProdFile = new File(".env.production");
			File destEnvFile = new File(".env");
			
			if ("prod".equals(profile) && envProdFile.exists()) {
				sourceEnvPath = Paths.get(".env.production");
				System.out.println("Using production environment file");
			} else if (envDevFile.exists()) {
				sourceEnvPath = Paths.get(".env.development");
				System.out.println("Using development environment file");
			} else {
				System.out.println("No environment files found. Using system environment variables.");
				return;
			}
			
			// Copy the appropriate env file to .env
			Files.copy(sourceEnvPath, destEnvFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Successfully copied " + sourceEnvPath + " to .env");
			
		} catch (IOException e) {
			System.err.println("Failed to set up environment files: " + e.getMessage());
		}
	}
}
