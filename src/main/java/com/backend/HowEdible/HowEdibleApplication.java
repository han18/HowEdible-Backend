package com.backend.HowEdible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HowEdibleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HowEdibleApplication.class, args);
		
		// to check the java version
		System.out.println(System.getProperty("java.version"));
		
//		to check the server is running 
		System.out.println("This project is the original version");
		System.out.println("This project has a git and a github repositiory called Backend-HowEdible");

		System.out.println("Server is running successfully ");

		
	}

}

