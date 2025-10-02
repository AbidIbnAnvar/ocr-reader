package com.abid.ocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class OcrApplication {

	static {
		try {
			// This loads your .env file from the current directory
			// where you run 'mvn spring-boot:run'
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		} catch (Exception e) {
			System.err.println(
					"WARN: Could not load .env file. Falling back to default environment variables. " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OcrApplication.class, args);
	}

}
