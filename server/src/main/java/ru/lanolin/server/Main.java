package ru.lanolin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {

	public static void main(String[] args) throws IOException {
//		Configuration configuration = Configuration.loadProperties();
		SpringApplication.run(Main.class, args);
	}

}
