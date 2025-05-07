package com.banking.first;
import com.banking.first.controllers.AccountController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;
@SpringBootApplication
public class Bankapp1Application {

	public static void main(String[] args) {
		new File (AccountController.uploadDirectory).mkdir();
		SpringApplication.run(Bankapp1Application.class, args);
	}

}
