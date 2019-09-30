package com.epam.texteditor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class TextEditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TextEditorApplication.class, args);
	}

}
