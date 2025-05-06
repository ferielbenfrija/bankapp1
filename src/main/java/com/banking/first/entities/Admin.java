package com.banking.first.entities;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.banking.first.repositories.UserRepository;

@Component

public class Admin  implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@bank.com") == null) {
            AppUser admin = new AppUser();
            admin.setFirstname("Admin");
            admin.setLastname("Bank");
            admin.setEmail("admin@bank.com");
            admin.setPhone("00000000");
            admin.setAddress("Siège social");
            admin.setRole("admin");
            admin.setCreatedAt(new Date());
            admin.setPassworld(new BCryptPasswordEncoder().encode("admin123"));
            admin.setBalance(0.0);
            userRepository.save(admin);
            System.out.println("Admin user created.");
        }
    }
}