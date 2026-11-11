package com.example.wordle_backend;

import com.example.wordle_backend.service.WordleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordleBackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(WordleBackendApplication.class, args);

	}

}
